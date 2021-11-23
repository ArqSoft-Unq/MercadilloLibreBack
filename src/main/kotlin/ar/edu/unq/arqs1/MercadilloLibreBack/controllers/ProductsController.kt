package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.ProductSpecification
import ar.edu.unq.arqs1.MercadilloLibreBack.lib.ProductSpecificationsBuilder
import ar.edu.unq.arqs1.MercadilloLibreBack.lib.SearchCriteria
import ar.edu.unq.arqs1.MercadilloLibreBack.listeners.newProductsTopic
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.ProductsResponse
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BulkCreateProcessor
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.regex.Pattern
import javax.validation.Valid

@RestController
@RequestMapping("/v1/products")
@Validated
class ProductsController(
    private val productsService: ProductService,
    private val businessService: BusinessService,
    private val bulkCreateProcessor: BulkCreateProcessor
) {
    @PostMapping
    fun addProduct(
        @AuthenticationPrincipal business: Business,
        @RequestBody @Valid newProduct: NewProduct
    ): ResponseEntity<Product> {
        return businessService.getBusinessById(business.id!!).map { business ->
            val product = newProduct.toProduct(business)
            ResponseEntity.ok(productsService.addProduct(product))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @AuthenticationPrincipal business: Business,
        @PathVariable(value = "id") productId: Long
    ): ResponseEntity<Nothing> =
        productsService.getProductById(productId)
            .map { product ->
                if (product.seller.id == business.id) {
                    product
                } else {
                    null
                }
            }
            .map { product -> productsService.deleteProduct(product?.id!!).orElse(null) }
            .map { ResponseEntity.ok(null) }
            .orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun updateProduct(
        @AuthenticationPrincipal business: Business,
        @PathVariable(value = "id") productId: Long,
        @RequestBody @Valid updateProduct: UpdateProduct
    ): ResponseEntity<Product> =

        productsService.getProductById(productId)
            .map { product ->
                if (product.seller.id == business.id) {
                    product
                } else {
                    null
                }
            }
            .map { product -> productsService.updateProduct(product?.id!!, updateProduct).orElse(null) }
            .map { product -> ResponseEntity.ok(product) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping("/{id}")
    fun getProduct(@PathVariable(value = "id") productId: Long): ResponseEntity<Product> =
        productsService.getProductById(productId)
            .map { product -> ResponseEntity.ok(product) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping
    fun allProducts(
        @RequestParam(value = "filters", required = false) filtersParam: Array<String>?,
        @RequestParam(value = "search", required = false) searchParam: String?
    ): ResponseEntity<ProductsResponse> {

        val filters = filtersParam ?: emptyArray()
        val filtersBuilder = ProductSpecificationsBuilder()
        filters.map { filter -> SEARCH_CRITERIA_PATTERN.matcher(filter) }.forEach { matcher ->
            if (matcher.find()) {
                filtersBuilder.with(
                    SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3))
                )
            }
        }
        val filtersSpecification = filtersBuilder.build()

        val searchSpecification = searchParam?.let {
            Specification.where(
                ProductSpecification(SearchCriteria("description", ":", searchParam)).or(
                    ProductSpecification(SearchCriteria("name", ":", searchParam))
                )
            )
        }

        val specification = searchSpecification?.let {
            filtersSpecification?.let { filtersSpecification.and(searchSpecification) } ?: searchSpecification
        } ?: filtersSpecification

        return ResponseEntity.ok(ProductsResponse(products = productsService.products(specification)))
    }

    @PostMapping("/bulkCreate")
    fun bulkCreate(@AuthenticationPrincipal business: Business, @RequestParam("file") file: MultipartFile) {
        bulkCreateProcessor.process(business, file)
    }

    companion object {
        val SEARCH_CRITERIA_PATTERN: Pattern = Pattern.compile("(.+)([:<>])(.+)")
    }
}
