package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class CreateBusinessTest : ApplicationTest() {

    @Test
    fun whenTheNameIsMissing_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/businesses",
                Business(email = "email@email.com"),
                Business::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsMissing_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/businesses",
                Business(name = "name"),
                Business::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheNameIsBlank_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/businesses",
                Business(name="", email = "email@email.com"),
                Business::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsBlank_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/businesses",
                Business(name = "name", email = ""),
                Business::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsNotAnEmail_thenReturnsStatus400() {
        val result = restTemplate.postForEntity(
                "/v1/businesses",
                Business(name = "name", email = "lala"),
                Business::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenAllTheFieldsAreValid_thenReturnsStatus200() {
        val business = Business(name = "name", email = "email@email.com")
        val result = restTemplate.postForEntity("/v1/businesses", business, Business::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertNotNull(result.body!!.id)
        assertEquals(business.name, result.body!!.name)
        assertEquals(business.email, result.body!!.email)
    }
}