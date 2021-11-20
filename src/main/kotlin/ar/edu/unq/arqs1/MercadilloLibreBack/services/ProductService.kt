package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService(private val productsRepository: ProductsRepository) {
    fun products(specification: Specification<Product>?): List<Product> {
        return productsRepository.findAll(specification)
    }

    fun addProduct(product: Product): Product =
        productsRepository.save(product)

    fun getProductById(productId: Long): Optional<Product> =
        productsRepository.findById(productId)

    fun updateProduct(id: Long, product: UpdateProduct): Optional<Product> {
        return getProductById(id).map { existentProduct ->
            if(product.name != null) { existentProduct.name = product.name }
            if(product.description != null) { existentProduct.description = product.description }
            if(product.price != null) { existentProduct.price = product.price }
            if(product.stock != null) { existentProduct.stock = product.stock }

            productsRepository.save(existentProduct)
            existentProduct
       }
    }

    fun deleteProduct(id: Long): Optional<Product> =
        getProductById(id).map { product ->
            product.isActive = false
            productsRepository.save(product)
            product
        }
}