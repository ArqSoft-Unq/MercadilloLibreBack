package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.ProductsResponse
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business.BusinessesRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.apache.coyote.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class GetProductsTest : ApplicationTest() {

    @Autowired
    lateinit var businessesRepository: BusinessesRepository

    @Autowired
    lateinit var productsRepository: ProductsRepository

    var business: Business? = null

    @BeforeEach
    fun setup() {
        business = businessesRepository.save(
            Business(name = "name", email = "email@amil.com", encryptedPassword = "asda")
        )
    }

    fun doRequest(params: Array<String> = emptyArray()): ResponseEntity<ProductsResponse> =
        restTemplate.getForEntity(
            "/v1/products?search={search}",
            ProductsResponse::class.java,
            mapOf("search" to params.joinToString(",") { it })
        )

    @Test
    fun `when there is no products the result is empty`() {
        val result = doRequest()
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(result.body!!.products, arrayListOf<Product>())
    }

    @Test
    fun `when exists some products without filter return all products`() {
        val product1 = productsRepository.save(
            Product(name = "p1", description = "product 1", price = 1, stock = 1, seller = business)
        )

        val product2 = productsRepository.save(
            Product(name = "p2", description = "product 2", price = 2, stock = 2, seller = business)
        )

        val result = doRequest()
        val productIds = result.body!!.products?.map { product -> product.id }

        assertEquals(HttpStatus.OK, result.statusCode)
        assertThat(result.body!!.products).hasSize(2)
        assertThat(product1.id).isIn(productIds)
        assertThat(product2.id).isIn(productIds)
    }

    @Test
    fun `when exists some products and filter by name return filtered products`() {
        val product1 = productsRepository.save(
            Product(name = "p1", description = "product 1", price = 1, stock = 1, seller = business)
        )

        val product2 = productsRepository.save(
            Product(name = "p2", description = "product 2", price = 2, stock = 2, seller = business)
        )

        val result = doRequest(arrayOf("name:p1"))
        val productIds = result.body!!.products?.map { product -> product.id }

        assertEquals(HttpStatus.OK, result.statusCode)
        assertThat(result.body!!.products).hasSize(1)
        assertThat(product1.id).isIn(productIds)
        assertThat(product2.id).isNotIn(productIds)
    }

    @Test
    fun `searching by more than one criteria`() {
        val product1 = productsRepository.save(
            Product(name = "p1", description = "product 1", price = 1, stock = 1, seller = business)
        )

        val product2 = productsRepository.save(
            Product(name = "p2", description = "product 2", price = 2, stock = 2, seller = business)
        )

        val result = doRequest(arrayOf("name:p", "price<2"))
        val productIds = result.body!!.products?.map { product -> product.id }

        assertEquals(HttpStatus.OK, result.statusCode)
        assertThat(result.body!!.products).hasSize(1)
        assertThat(product1.id).isIn(productIds)
        assertThat(product2.id).isNotIn(productIds)
    }

    @Test
    fun `searching by seller`() {
        val product1 = productsRepository.save(
            Product(name = "p1", description = "product 1", price = 1, stock = 1, seller = business)
        )

        val product2 = productsRepository.save(
            Product(name = "p2", description = "product 2", price = 2, stock = 2, seller = business)
        )

        val result = doRequest(arrayOf("sellerId:${business!!.id}"))
        val productIds = result.body!!.products?.map { product -> product.id }

        assertEquals(HttpStatus.OK, result.statusCode)
        assertThat(result.body!!.products).hasSize(2)
        assertThat(product1.id).isIn(productIds)
        assertThat(product2.id).isIn(productIds)
    }
}