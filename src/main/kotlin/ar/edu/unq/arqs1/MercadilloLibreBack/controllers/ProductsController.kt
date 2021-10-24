package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
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
    fun allProducts(): ResponseEntity<MutableList<Product>> =
        ResponseEntity.ok(productsService.products())
}
