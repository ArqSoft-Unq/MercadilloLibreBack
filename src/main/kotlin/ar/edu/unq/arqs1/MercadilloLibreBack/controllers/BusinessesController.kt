package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.JwtTokenUtil
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.LoginService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController @RequestMapping("/v1/businesses")
@Validated
class BusinessesController(
    private val businessService: BusinessService,
    private val passwordEncoder: PasswordEncoder,
    private val loginService: LoginService) {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleValidationErrors(error: DataIntegrityViolationException) =
        mapOf("error" to error.message)

    @PostMapping("/login")
    fun login(@RequestBody @Valid credentials: Credentials): ResponseEntity<Unit> {
        val businessFetcher = { email:String -> businessService.getBusinessByEmail(email) as Optional<UserDetails> }
        return loginService.doLogin(credentials, businessFetcher)
    }

    @PostMapping
    fun addUser(@RequestBody @Valid newBusiness: NewBusiness): ResponseEntity<Business> {
        val business = newBusiness.toBusiness()
        business.encryptedPassword = passwordEncoder.encode(newBusiness.password)
        return ResponseEntity.ok(businessService.addBusiness(business))
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable(value = "id") businessId: Long): ResponseEntity<Business> =
        businessService.getBusinessById(businessId).map { businesses -> ResponseEntity.ok(businesses) }
            .orElse(ResponseEntity.notFound().build())

}