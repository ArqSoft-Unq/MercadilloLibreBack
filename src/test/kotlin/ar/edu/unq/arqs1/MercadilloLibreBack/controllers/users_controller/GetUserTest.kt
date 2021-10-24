package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewUser
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.*

class GetUserTest : ApplicationTest() {
    @Test
    fun whenTheUserIdIsFromAnExistentUser_thenReturnsStatus200() {
        val newUser = NewUser(name = "name", lastname = "lastname", email = "email@email.com", password = "asds")
        val user = createUser(newUser)
        val result = userAuthenticatedExchange(
            Credentials(newUser.email, newUser.password),
            "/v1/users/info",
            HttpMethod.GET,
            null,
            User::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(user.id, result.body!!.id)
        assertEquals(user.name, result.body!!.name)
        assertEquals(user.lastname, result.body!!.lastname)
        assertEquals(user.email, result.body!!.email)
    }
}