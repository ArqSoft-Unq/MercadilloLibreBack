package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.JwtTokenUtil
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginService(private val passwordEncoder: PasswordEncoder) {
    fun doLogin(credentials: Credentials, entityFetcher: (String) -> Optional<UserDetails>): ResponseEntity<Unit> {
        val maybeEntity = entityFetcher(credentials.email!!)
        val passwordMatches = maybeEntity
            .map { user -> passwordEncoder.matches(credentials.password, user.password) }
            .orElse(false)

        return if (passwordMatches) {
            ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                JwtTokenUtil.generateToken(maybeEntity.get())
            ).body(null)
        } else {
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
}