package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.products_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.UpdateProduct
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


class UpdateProductTest : ApplicationTest() {

    @Autowired
    lateinit var productsRepository: ProductsRepository

    private var seller: Business? = null
    private var product: Product? = null

    @BeforeEach
    fun setUp() {
        val newBusiness = NewBusiness(name = "name", email = "email@email.com", password = "sarlanga")
        credentials = Credentials(newBusiness.email, newBusiness.password)
        seller = createBusiness(newBusiness)

        product = productsRepository.save(
            Product(
                name = "product", description = "descrption",
                price = 10, stock = 10, seller = seller!!, isActive = true
            )
        )
    }

    fun updateProductRequest(product: UpdateProduct): ResultActions {
        return businessAuthenticatedExchange(
            credentials!!,
            "/v1/products/${product.id}",
            HttpMethod.PUT,
            product
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
        updateProductRequest(updateProduct(name = ""))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun whenDescriptionIsEmpty_thenReturnsStatus400() {
        updateProductRequest(updateProduct(description = ""))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun whenPriceIsBellow0_thenReturnsStatus400() {
        updateProductRequest(updateProduct(price = -1))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun whenStockIsBellow0_thenReturnsStatus400() {
        updateProductRequest(updateProduct(stock = -1))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun updatesTheName() {
        val newName = "new name"
        updateProductRequest(updateProduct(name = newName))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.`is`(newName)))
    }

    @Test
    fun updatesTheDescription() {
        val newDescription = "new description"
        updateProductRequest(updateProduct(description = newDescription))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.`is`(newDescription)))
    }

    @Test
    fun updatesTheStock() {
        val newStock = 1
        updateProductRequest(updateProduct(stock = newStock))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.stock", CoreMatchers.`is`(newStock)))
    }

    @Test
    fun updatesThePrice() {
        val newPrice = 100000
        updateProductRequest(updateProduct(price = newPrice))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.`is`(newPrice)))
    }

    @Test
    fun whenTheProductIdIsDoesNotExists_thenReturns404() {
        val updateProduct = updateProduct()
        updateProduct.id = product!!.id?.plus(1)
        updateProductRequest(updateProduct)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}