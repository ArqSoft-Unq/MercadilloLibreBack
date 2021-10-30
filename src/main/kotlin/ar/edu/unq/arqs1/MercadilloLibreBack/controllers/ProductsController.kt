package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.ProductSpecificationsBuilder
import ar.edu.unq.arqs1.MercadilloLibreBack.lib.SearchCriteria
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.ProductsResponse
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.regex.Pattern
import javax.validation.Valid

@RestController
@RequestMapping("/v1/products")
@Validated
class ProductsController(private val productsService: ProductService, private val businessService: BusinessService) {
    @PostMapping
    fun addProduct(@AuthenticationPrincipal business: Business, @RequestBody @Valid newProduct: NewProduct): ResponseEntity<Product> {
        return businessService.getBusinessById(business.id!!).map { business ->
            val product = newProduct.toProduct()
            product.seller = business
            ResponseEntity.ok(productsService.addProduct(product))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@AuthenticationPrincipal business: Business, @PathVariable(value="id") productId: Long): ResponseEntity<Nothing> =
        productsService.getProductById(productId)
            .map { product -> if (product.seller?.id == business.id) {product} else {null} }
            .map { product -> productsService.deleteProduct(product?.id!!).orElse(null) }
            .map { ResponseEntity.ok(null) }
            .orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun updateProduct(
        @AuthenticationPrincipal business: Business,
        @PathVariable(value="id") productId: Long,
        @RequestBody @Valid updateProduct: UpdateProduct): ResponseEntity<Product> =

        productsService.getProductById(productId)
            .map { product -> if (product.seller?.id == business.id) {product} else {null} }
            .map { product -> productsService.updateProduct(product?.id!!, updateProduct).orElse(null) }
            .map {product -> ResponseEntity.ok(product) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping("/{id}")
    fun getProduct(@PathVariable(value = "id") productId: Long): ResponseEntity<Product> =
        productsService.getProductById(productId)
            .map { product -> ResponseEntity.ok(product) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping
    fun allProducts(
        @RequestParam(value = "search", required = false) searchParam: Array<String>?): ResponseEntity<ProductsResponse> {
        val search = searchParam ?: emptyArray()
        val productSpecificationsBuilder = ProductSpecificationsBuilder()
        search.map { searchCriteria -> SEARCH_CRITERIA_PATTERN.matcher(searchCriteria) }.forEach { matcher ->
            if (matcher.find()) {
                productSpecificationsBuilder.with(
                    SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3))
                )
            }
        }

        return ResponseEntity.ok(ProductsResponse(products=productsService.products(productSpecificationsBuilder.build())))
    }

    companion object {
        val SEARCH_CRITERIA_PATTERN: Pattern = Pattern.compile("(.+)([:<>])(.+)")
    }
}
