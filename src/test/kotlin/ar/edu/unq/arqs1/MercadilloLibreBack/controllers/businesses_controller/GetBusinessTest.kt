package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business.BusinessesRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class GetBusinessTest : ApplicationTest() {

    @Test
    fun whenTheUserIdIsFromAnExistentBusiness_thenReturnsStatus200() {
        val newBusiness = NewBusiness(name = "name", email = "email@email.com", password = "asd")
        val business = createBusiness(newBusiness)
        val result = businessAuthenticatedExchange(
            Credentials(newBusiness.email, newBusiness.password),
            "/v1/businesses/info",
            HttpMethod.GET,
            null,
            Business::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(business.id, result.body!!.id)
        assertEquals(business.name, result.body!!.name)
        assertEquals(business.email, result.body!!.email)
    }
}