package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.products_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


class UpdateProductTest : ApplicationTest() {

    @Autowired
    lateinit var productsRepository: ProductsRepository

    private var seller: Business? = null
    private var credentials: Credentials? = null
    private var product: Product? = null

    @BeforeEach
    fun setUp() {
        val newBusiness = NewBusiness(name = "name", email = "email@email.com", password = "sarlanga")
        credentials = Credentials(newBusiness.email, newBusiness.password)
        seller = createBusiness(newBusiness)

        product = productsRepository.save(
            Product(
                name = "product", description = "descrption",
                price = 10, stock = 10, seller = seller, isActive = true
            )
        )
    }

    fun updateProductRequest(product: UpdateProduct): ResponseEntity<Product> {
        return businessAuthenticatedExchange(
            credentials!!,
            "/v1/products/${product.id}",
            HttpMethod.PUT,
            product,
            Product::class.java
        )
    }

    fun updateProduct(
        id: Long? = product!!.id,
        name: String? = null,
        description: String? = null,
        price: Int? = null,
        stock: Int? = null,
        sellerId: Long? = null
    ) =

        UpdateProduct(id, name, description, price, stock)

    @Test
    fun whenNameIsEmpty_thenReturnsStatus400() {
        val result = updateProductRequest(updateProduct(name = ""))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenDescriptionIsEmpty_thenReturnsStatus400() {
        val result = updateProductRequest(updateProduct(description = ""))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenPriceIsBellow0_thenReturnsStatus400() {
        val result = updateProductRequest(updateProduct(price = -1))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenStockIsBellow0_thenReturnsStatus400() {
        val result = updateProductRequest(updateProduct(stock = -1))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun updatesTheName() {
        val newName = "new name"
        val result = updateProductRequest(updateProduct(name = newName))
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(newName, result.body!!.name)
    }

    @Test
    fun updatesTheDescription() {
        val newDescription = "new description"
        val result = updateProductRequest(updateProduct(description = newDescription))
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(newDescription, result.body!!.description)
    }

    @Test
    fun updatesTheStock() {
        val newStock = 1
        val result = updateProductRequest(updateProduct(stock = newStock))
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(newStock, result.body!!.stock)
    }

    @Test
    fun updatesThePrice() {
        val newPrice = 100000
        val result = updateProductRequest(updateProduct(price = newPrice))
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(newPrice, result.body!!.price)
    }

    @Test
    fun whenTheProductIdIsDoesNotExists_thenReturns404() {
        val updateProduct = updateProduct()
        updateProduct.id = product!!.id?.plus(1)
        val result = updateProductRequest(updateProduct)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }
}