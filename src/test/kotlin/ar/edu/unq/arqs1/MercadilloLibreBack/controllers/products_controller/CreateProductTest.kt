package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.products_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreateProductTest : ApplicationTest() {

    private var seller: Business? = null

    @BeforeEach
    fun setUp() {
        val newBusiness = NewBusiness(name = "name", email = "email@email.com", password = "sarlanga")
        credentials = Credentials(newBusiness.email, newBusiness.password)
        seller = createBusiness(newBusiness)
    }

    fun createProduct(product: NewProduct): ResultActions =
        businessAuthenticatedExchange(credentials!!, "/v1/products", HttpMethod.POST, product)

    fun newProduct(
        name: String? = "name",
        description: String? = "description",
        price: Int? = 10,
        stock: Int? = 10
    ) =

        NewProduct(name, description, price, stock)

    @Test
    fun whenTheNameIsMissing_thenReturnsStatus400() {
        createProduct(newProduct(name = null))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenTheDescriptionIsMissing_thenReturnsStatus400() {
        createProduct(newProduct(description = null))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenThePriceIsMissing_thenReturnsStatus400() {
        createProduct(newProduct(price = null))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenTheStockIsMissing_thenReturnsStatus400() {
        createProduct(newProduct(stock = null))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenTheNameIsBlank_thenReturnsStatus400() {
        createProduct(newProduct(name = ""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenTheDescriptionBlank_thenReturnsStatus400() {
        createProduct(newProduct(description = ""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenPriceIsBellow0_thenReturnsStatus400() {
        createProduct(newProduct(price = -1))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenStockIsBellow0_thenReturnsStatus400() {
        createProduct(newProduct(stock = -1))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAllTheFieldsAreValid_thenReturnsStatus200() {
        val newProduct: NewProduct = newProduct()
        createProduct(newProduct)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.name", `is`(newProduct.name)))
            .andExpect(jsonPath("$.description", `is`(newProduct.description)))
            .andExpect(jsonPath("$.price", `is`(newProduct.price)))
            .andExpect(jsonPath("$.stock", `is`(newProduct.stock)))
    }
}