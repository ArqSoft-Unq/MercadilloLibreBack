package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateOrderTest : ApplicationTest() {

    private var credentials: Credentials? = null

    fun postOrder(authenticated: Boolean): ResponseEntity<Order> =
        if(authenticated) {
            userAuthenticatedExchange(credentials!!, "/v1/orders", HttpMethod.POST, null, Order::class.java)
        } else {
            restTemplate.postForEntity("/v1/orders", null, Order::class.java)
        }

    @BeforeEach
    fun setUp() {
        credentials = createAUser()
    }

    @Test
    fun `creates the order`() {
        val result = postOrder(authenticated = true)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertNotNull(result.body!!.id)
        assertEquals(Order.Status.PENDING.code, result.body!!.status)
    }

    @Test
    fun `it fails with unauthorized`() {
        val result = postOrder(authenticated = false)
        assertEquals(HttpStatus.UNAUTHORIZED, result.statusCode)
    }
}

