package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.services.UserService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/users")
@Validated
class UsersController(private val userService: UserService) {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleValidationErrors(error: DataIntegrityViolationException) =
        mapOf("error" to error.message)

    @PostMapping
    fun addUser(@RequestBody @Valid user: User): ResponseEntity<User> {
        return userService.addUser(user)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable(value = "id") userId: Long): ResponseEntity<User> = userService.getUserById(userId)
}