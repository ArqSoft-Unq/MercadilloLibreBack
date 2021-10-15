package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.BusinessesRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.ProductsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class GetProductTest : ApplicationTest() {

    @Autowired
    lateinit var businessesRepository: BusinessesRepository

    @Autowired
    lateinit var productsRepository: ProductsRepository

    @Test
    fun whenTheUserIdIsNotFromAnExistentProduct_thenReturnsStatus404() {
        val result = restTemplate.getForEntity("/v1/products/1", Product::class.java)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }


    @Test
    fun whenTheUserIdIsFromAnExistentProduct_thenReturnsStatus200() {
        val business = businessesRepository.save(Business(name = "name", email = "email@email.co"))
        val product = productsRepository.save(Product(name = "name", description = "something", price = 10, stock = 10, seller = business))
        val result = restTemplate.getForEntity("/v1/products/${product.id}", Product::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(product.id, result.body!!.id)
        assertEquals(product.name, result.body!!.name)
        assertEquals(product.description, result.body!!.description)
        assertEquals(product.price, result.body!!.price)
        assertEquals(product.stock, result.body!!.stock)
        assertEquals(product.seller!!.id, result.body!!.seller!!.id)
    }
}