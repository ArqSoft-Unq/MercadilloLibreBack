package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.BusinessesRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class GetBusinessTest : ApplicationTest() {

    @Autowired
    lateinit var businessesRepository: BusinessesRepository

    @Test
    fun whenTheUserIdIsNotFromAnExistentUser_thenReturnsStatus404() {
        val result = restTemplate.getForEntity("/v1/businesses/1", Business::class.java)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun whenTheUserIdIsFromAnExistentUser_thenReturnsStatus200() {
        val business = businessesRepository.save(Business(name = "name", email = "email@email.com"))
        val result = restTemplate.getForEntity("/v1/businesses/${business.id}", Business::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(business.id, result.body!!.id)
        assertEquals(business.name, result.body!!.name)
        assertEquals(business.email, result.body!!.email)
    }
}