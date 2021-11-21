package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewUser
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class GetUserTest : ApplicationTest() {
    @Test
    fun whenTheUserIdIsFromAnExistentUser_thenReturnsStatus200() {
        val newUser = NewUser(name = "name", lastname = "lastname", email = "email@email.com", password = "asds")
        val user = createUser(newUser)
        userAuthenticatedExchange(
            Credentials(newUser.email, newUser.password),
            "/v1/users/info",
            HttpMethod.GET,
            null
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.`is`(user.id!!.toInt())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.`is`(user.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.`is`(user.lastname)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.`is`(user.email)))
    }
}