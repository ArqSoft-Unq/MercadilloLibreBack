package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.products_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.BusinessesRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateProductTest : ApplicationTest() {

    @Autowired
    final lateinit var businessesRepository: BusinessesRepository

    private var seller: Business? = null

    @BeforeEach
    fun setUp() {
        seller = businessesRepository.save(Business(name = "name", email = "email@email.com"))
    }

    fun createProduct(product: NewProduct) : ResponseEntity<Product> =
        restTemplate.postForEntity("/v1/products", product, Product::class.java)

    fun newProduct(
        name: String? = "name",
        description: String? = "description",
        price: Int? = 10,
        stock: Int? = 10,
        sellerId: Long? = seller!!.id) =

        NewProduct(name, description, price, stock, sellerId)

    @Test
    fun whenTheNameIsMissing_thenReturnsStatus400() {
        val result = createProduct(newProduct(name = null))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheDescriptionIsMissing_thenReturnsStatus400() {
        val result = createProduct(newProduct(description = null))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenThePriceIsMissing_thenReturnsStatus400() {
        val result = createProduct(newProduct(price = null))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheStockIsMissing_thenReturnsStatus400() {
        val result = createProduct(newProduct(stock = null))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheSellerIdIsMissing_thenReturnsStatus400() {
        val result = createProduct(newProduct(sellerId = null))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheNameIsBlank_thenReturnsStatus400() {
        val result = createProduct(newProduct(name = ""))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheDescriptionBlank_thenReturnsStatus400() {
        val result = createProduct(newProduct(description = ""))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenPriceIsBellow0_thenReturnsStatus400() {
        val result = createProduct(newProduct(price = -1))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenStockIsBellow0_thenReturnsStatus400() {
        val result = createProduct(newProduct(stock = -1))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenSellerIdIsNotFromAnExistentSeller_thenReturnsStatus404() {
        val sellerId: Long = seller!!.id?.let { seller!!.id } ?: run { 0 }
        val result = createProduct(newProduct(sellerId = sellerId + 1))
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun whenAllTheFieldsAreValid_thenReturnsStatus200() {
        val newProduct: NewProduct = newProduct()
        val result = createProduct(newProduct)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertNotNull(result.body!!.id)
        assertEquals(newProduct.name, result.body!!.name)
        assertEquals(newProduct.description, result.body!!.description)
        assertEquals(newProduct.price, result.body!!.price)
        assertEquals(newProduct.stock, result.body!!.stock)
        assertEquals(newProduct.sellerId, result.body!!.seller!!.id)
    }
}