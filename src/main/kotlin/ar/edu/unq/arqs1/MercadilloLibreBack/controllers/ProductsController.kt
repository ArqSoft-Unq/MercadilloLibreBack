package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/products")
@Validated
class ProductsController(private val productsService: ProductService, private val businessService: BusinessService) {
    @PostMapping
    fun addProduct(@RequestBody @Valid newProduct: NewProduct): ResponseEntity<Product> {
        return businessService.getBusinessById(newProduct.sellerId!!).map { business ->
            val product = newProduct.toProduct()
            product.seller = business
            ResponseEntity.ok(productsService.addProduct(product))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable(value="id") productId: Long): ResponseEntity<Nothing> =
            productsService.deleteProduct(productId)
                .map { ResponseEntity.ok(null) }
                .orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable(value="id") productId: Long,
        @RequestBody @Valid updateProduct: UpdateProduct): ResponseEntity<Product> =

        productsService.updateProduct(productId, updateProduct)
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
