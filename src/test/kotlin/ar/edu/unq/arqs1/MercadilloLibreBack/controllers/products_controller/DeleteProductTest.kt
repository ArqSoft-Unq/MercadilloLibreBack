package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business.BusinessesRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*

class DeleteProductTest : ApplicationTest() {

    @Autowired
    lateinit var businessesRepository: BusinessesRepository

    @Autowired
    lateinit var productsRepository: ProductsRepository

    private var seller: Business? = null
    private var product: Product? = null

    @BeforeEach
    fun setUp() {
        seller = businessesRepository.save(Business(
            name = "name", email = "email@email.com", encryptedPassword = "something")
        )
        product = productsRepository.save(Product(
            name = "product",
            description = "descrption",
            price = 10,
            stock = 10,
            seller = seller,
            isActive = true
        ))
    }

    fun deleteProductRequest(productId: Long): ResponseEntity<Product> {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val entity = HttpEntity<UpdateProduct>(null, headers)
        return restTemplate.exchange("/v1/products/${productId}", HttpMethod.DELETE, entity, Product::class.java)
    }

    @Test
    fun whenTheUserIdIsNotFromAnExistentProduct_thenReturnsStatus404() {
        val result = deleteProductRequest(product!!.id?.plus(1) ?: 0)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }


    @Test
    fun whenTheUserIdIsFromAnExistentProduct_thenReturnsStatus200() {
        val result = product!!.id?.let { deleteProductRequest(it) }

        assertEquals(HttpStatus.OK, result!!.statusCode)
        assertNull(result.body)
    }
}