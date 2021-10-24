package ar.edu.unq.arqs1.MercadilloLibreBack.lib

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


class JwtTokenUtil(private val jwtToken: String) {

    fun usernameFromToken(): String =
        allClaimsFromToken().subject

    fun userTypeFromToken(): String =
        claimFromTokenByName("type")

    private fun expirationDateFromToken(): Date =
        allClaimsFromToken().expiration

    fun claimFromTokenByName(name: String): String =
        allClaimsFromToken()[name] as String

    private fun allClaimsFromToken(): Claims =
        Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken).body

    private fun isTokenExpired(): Boolean =
        expirationDateFromToken().before(Date())

    fun validateToken(userDetails: UserDetails): Boolean =
        usernameFromToken() == userDetails.username && !isTokenExpired()

    fun validateToken(): Boolean {
        return !isTokenExpired()
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = (24 * 60 * 60 * 1000).toLong()
        const val SECRET = "mysecret"

        fun generateToken(userDetails: UserDetails): String {
            val claims = addClaims(userDetails)
            return doGenerateToken(claims, userDetails.username!!)
        }

        private fun addClaims(userDetails: UserDetails): Map<String, Any> {
            return mutableMapOf("type" to userDetails::class.toString())
        }

        private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
            return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact()
        }
    }

}
