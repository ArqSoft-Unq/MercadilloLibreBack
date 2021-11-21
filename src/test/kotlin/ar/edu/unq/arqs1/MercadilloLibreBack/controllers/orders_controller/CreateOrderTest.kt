package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreateOrderTest : ApplicationTest() {
    @BeforeEach
    fun setUp() {
        credentials = createAUser()
    }

    @Test
    fun `creates the order`() {
        postOrder(authenticated = true)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.status", `is`(Order.Status.PENDING.code)))
    }

    @Test
    fun `it fails with unauthorized`() {
        postOrder(authenticated = false)
            .andExpect(status().isUnauthorized)
    }
}

