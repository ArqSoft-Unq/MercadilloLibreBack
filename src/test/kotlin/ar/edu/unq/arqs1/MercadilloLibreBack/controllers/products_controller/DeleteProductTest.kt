package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class DeleteProductTest : ApplicationTest() {

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
                name = "product",
                description = "descrption",
                price = 10,
                stock = 10,
                seller = seller!!,
                isActive = true
            )
        )
    }

    fun deleteProductRequest(productId: Long): ResultActions {
        return businessAuthenticatedExchange(
            credentials!!,
            "/v1/products/${productId}",
            HttpMethod.DELETE,
            null
        )
    }

    @Test
    fun whenTheUserIdIsNotFromAnExistentProduct_thenReturnsStatus404() {
        deleteProductRequest(product!!.id?.plus(1) ?: 0)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }


    @Test
    fun whenTheUserIdIsFromAnExistentProduct_thenReturnsStatus200() {
        deleteProductRequest(productId = product!!.id!!)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}