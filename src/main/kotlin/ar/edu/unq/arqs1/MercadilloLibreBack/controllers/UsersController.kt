package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.JwtTokenUtil
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewUser
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.LoginResult
import ar.edu.unq.arqs1.MercadilloLibreBack.services.LoginService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.UserService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/users")
@Validated
class UsersController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val loginService: LoginService) {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleValidationErrors(error: DataIntegrityViolationException) =
        mapOf("error" to error.message)

    @PostMapping("/login")
    fun login(@RequestBody @Valid credentials: Credentials): ResponseEntity<LoginResult> {
        val userFetcher: (String) -> Optional<UserDetails> =
            { email -> userService.getUserByEmail(email) as Optional<UserDetails> }
        return loginService.doLogin(credentials, userFetcher)
    }

    @PostMapping
    fun addUser(@RequestBody @Valid newUser: NewUser): ResponseEntity<User> {
        val user = newUser.toUser()
        user.encryptedPassword = passwordEncoder.encode(newUser.password)

        return ResponseEntity.ok(userService.addUser(user))
    }

    @GetMapping("/info")
    fun getUser(@AuthenticationPrincipal user: User): ResponseEntity<User> = userService.getUserById(user.id!!)
}