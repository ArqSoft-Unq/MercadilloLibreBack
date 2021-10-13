package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class CreateUserTest : ApplicationTest() {

    @Test
    fun whenTheNameIsMissing_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(lastname = "lastname", email = "email@email.com"),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheLastNameIsMissing_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(name = "name", email = "email@email.com"),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsMissing_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(name = "name", lastname = "lastname"),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheNameIsBlank_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(name="", lastname = "lastname", email = "email@email.com"),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }
    @Test
    fun whenTheLastNameIsBlank_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(name = "name", lastname = "", email = "email@email.com"),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsBlank_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(name = "name", lastname = "lastname", email = ""),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsNotAnEmail_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/users",
                User(name = "name", lastname = "lastname", email = "lala"),
                User::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenAllTheFieldsAreValid_thenReturnsStatus200() {
        val user = User(name = "name", lastname = "lastname", email = "email@email.com")
        val result = restTemplate.postForEntity("/v1/users", user, User::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertNotNull(result.body!!.name)
        assertEquals(user.name, result.body!!.name)
        assertEquals(user.lastname, result.body!!.lastname)
        assertEquals(user.email, result.body!!.email)
    }
}