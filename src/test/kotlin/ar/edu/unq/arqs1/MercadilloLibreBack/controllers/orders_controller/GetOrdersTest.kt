package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewUser
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetOrdersTest : ApplicationTest() {
    private var buyer: User? = null

    @Autowired
    lateinit var ordersService: OrdersService

    fun getOrders(authenticated: Boolean): ResultActions =
        userAuthenticatedExchange(credentials!!, "/v1/orders", HttpMethod.GET, null)

    @BeforeEach
    fun setUp() {
        val newUser = NewUser(name = "name", lastname = "lala", email = "email@email.com", password = "sarlanga")
        credentials = Credentials(newUser.email, newUser.password)
        buyer = this.createUser(newUser)
    }

    @Test
    fun `if the user does not has orders it returns an empty list`() {
        getOrders(true)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", `is`(emptyList<Order>())))
    }

    @Test
    fun `if the user has orders it returns the list of them`() {
        val order = ordersService.addOrder(buyer!!)
        getOrders(true)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$[0].id", `is`(order.id!!.toInt())))
    }

    @Test
    fun `if the user is not authenticated it gets a 401`() {
        val response = restTemplate.exchange("/v1/orders", HttpMethod.GET, null, Any::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }


}