package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/users")
@Validated
class UsersController(private val userService: UserService) {
    @PostMapping
    fun addUser(@RequestBody @Valid user: User): ResponseEntity<User> {
        return userService.addUser(user)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable(value = "id") userId: Long): ResponseEntity<User> = userService.getUserById(userId)
}