package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetBusinessTest : ApplicationTest() {

    @Test
    fun whenTheUserIdIsFromAnExistentBusiness_thenReturnsStatus200() {
        val newBusiness = NewBusiness(name = "name", email = "email@email.com", password = "asd")
        val business = createBusiness(newBusiness)
        businessAuthenticatedExchange(
            Credentials(newBusiness.email, newBusiness.password),
            "/v1/businesses/info",
            HttpMethod.GET,
            null
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", `is`(business.id!!.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", `is`(business.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", `is`(business.email)))
    }
}