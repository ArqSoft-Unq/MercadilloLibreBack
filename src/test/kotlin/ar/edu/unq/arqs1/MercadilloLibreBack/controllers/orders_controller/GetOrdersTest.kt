package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business.BusinessesRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class GetOrdersTest : ApplicationTest() {

    class OrderResponse : ArrayList<Order>()

    private var credentials: Credentials? = null
    private var buyer: User? = null

    @Autowired
    lateinit var ordersService: OrdersService

    fun getOrders(authenticated: Boolean): ResponseEntity<OrderResponse> =
        userAuthenticatedExchange(credentials!!, "/v1/orders", HttpMethod.GET, null, OrderResponse::class.java)

    @BeforeEach
    fun setUp() {
        val newUser = NewUser(name = "name", lastname = "lala", email = "email@email.com", password = "sarlanga")
        credentials = Credentials(newUser.email, newUser.password)
        buyer = this.createUser(newUser)
    }

    @Test
    fun `if the user does not has orders it returns an empty list`() {
        val response: ResponseEntity<OrderResponse> = getOrders(true)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(emptyList<OrderResponse>(), response.body!!)
    }

    @Test
    fun `if the user has orders it returns the list of them`() {
        val order = ordersService.addOrder(buyer!!)
        val response = getOrders(true)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body!!.size)
        assertEquals(order.id, response.body!![0].id)
    }

    @Test
    fun `if the user is not authenticated it gets a 401`() {
        val response = restTemplate.exchange("/v1/orders", HttpMethod.GET, null, Any::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }


}