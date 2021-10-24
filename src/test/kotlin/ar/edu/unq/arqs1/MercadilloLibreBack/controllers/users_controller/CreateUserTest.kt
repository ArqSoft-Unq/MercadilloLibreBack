package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewUser
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.user.UsersRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateUserTest : ApplicationTest() {

    @Autowired
    lateinit var userRepository: UsersRepository

    fun postUser(newUser: NewUser) =
        restTemplate.postForEntity("/v1/users", newUser, User::class.java)

    @Test
    fun whenTheNameIsMissing_thenReturnsStatus400() {
        val result = postUser(NewUser(lastname = "lastname", email = "email@email.com", password = "asd"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheLastNameIsMissing_thenReturnsStatus400() {
        val result = postUser(NewUser(name = "name", email = "email@email.com", password = "asda"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsMissing_thenReturnsStatus400() {
        val result = postUser(NewUser(name = "name", lastname = "lastname", password = "asd"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheNameIsBlank_thenReturnsStatus400() {
        val result = postUser(NewUser(name="", lastname = "lastname", email = "email@email.com", password = "asd"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheLastNameIsBlank_thenReturnsStatus400() {
        val result = postUser(NewUser(name = "name", lastname = "", email = "email@email.com", password = "asd"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsBlank_thenReturnsStatus400() {
        val result = postUser(NewUser(name = "name", lastname = "lastname", email = "", password = "asd"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenEmailIsNotAnEmail_thenReturnsStatus400() {
        val result = postUser(NewUser(name = "name", lastname = "lastname", email = "lala", password = "asda"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenTheEmailIsDuplicated_thenReturnsStatus400() {
        val email = "email@email.com"
        userRepository.save(User(name = "name", lastname = "lastname", email = email, encryptedPassword = "asasd"))

        val result = postUser(NewUser(name = "other name", lastname = "other lastname", email = email, password = "asd"))
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun whenAllTheFieldsAreValid_thenReturnsStatus200() {
        val user = NewUser(name = "name", lastname = "lastname", email = "email@email.com", password = "asda")
        val result = postUser(user)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertNotNull(result.body!!.id)
        assertEquals(user.name, result.body!!.name)
        assertEquals(user.lastname, result.body!!.lastname)
        assertEquals(user.email, result.body!!.email)
    }
}