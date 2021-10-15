package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.BusinessesRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateBusinessTest : ApplicationTest() {

    @Autowired
    lateinit var businessesRepository: BusinessesRepository

    fun createBusiness(business: Business): ResponseEntity<Business> =
        restTemplate.postForEntity("/v1/businesses", business, Business::class.java)

    @Test
    fun whenTheNameIsMissing_thenReturnsStatus400() {
        val result = createBusiness(Business(email = "email@email.com"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsMissing_thenReturnsStatus400() {
        val result = createBusiness(Business(name = "name"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheNameIsBlank_thenReturnsStatus400() {
        val result = createBusiness(Business(name="", email = "email@email.com"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsBlank_thenReturnsStatus400() {
        val result = createBusiness(Business(name = "name", email = ""))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsNotAnEmail_thenReturnsStatus400() {
        val result = createBusiness(Business(name = "name", email = "lala"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheEmailIsDuplicated_thenReturnsStatus400() {
        val email = "email@email.com"
        businessesRepository.save(Business(name = "name", email = email))

        val result = createBusiness(Business(name = "other name", email = email))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenAllTheFieldsAreValid_thenReturnsStatus200() {
        val business = Business(name = "name", email = "email@email.com")
        val result = createBusiness(business)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertNotNull(result.body!!.id)
        assertEquals(business.name, result.body!!.name)
        assertEquals(business.email, result.body!!.email)
    }
}