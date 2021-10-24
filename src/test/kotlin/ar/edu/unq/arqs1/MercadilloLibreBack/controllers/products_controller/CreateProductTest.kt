package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.products_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateProductTest : ApplicationTest() {

    private var seller: Business? = null
    private var credentials: Credentials? = null

    @BeforeEach
    fun setUp() {
        val newBusiness = NewBusiness(name = "name", email = "email@email.com", password = "sarlanga")
        credentials = Credentials(newBusiness.email, newBusiness.password)
        seller = createBusiness(newBusiness)
    }

    fun createProduct(product: NewProduct): ResponseEntity<Product> =
        businessAuthenticatedExchange(credentials!!, "/v1/products", HttpMethod.POST, product, Product::class.java)

    fun newProduct(
        name: String? = "name",
        description: String? = "description",
        price: Int? = 10,
        stock: Int? = 10
    ) =

        NewProduct(name, description, price, stock)

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
    }
}