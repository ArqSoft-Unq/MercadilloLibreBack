package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.user.UsersRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class GetUserTest : ApplicationTest() {

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Test
    fun whenTheUserIdIsNotFromAnExistentUser_thenReturnsStatus404() {
        val result = restTemplate.getForEntity("/v1/users/1", User::class.java)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun whenTheUserIdIsFromAnExistentUser_thenReturnsStatus200() {
        val user = usersRepository.save(User(name = "name", lastname = "lastname", email = "email@email.com"))
        val result = restTemplate.getForEntity("/v1/users/${user.id}", User::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(user.id, result.body!!.id)
        assertEquals(user.name, result.body!!.name)
        assertEquals(user.lastname, result.body!!.lastname)
        assertEquals(user.email, result.body!!.email)
    }
}