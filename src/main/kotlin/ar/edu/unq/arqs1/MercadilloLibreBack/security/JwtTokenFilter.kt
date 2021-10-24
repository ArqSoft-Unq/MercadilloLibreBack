package ar.edu.unq.arqs1.MercadilloLibreBack.security

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.JwtTokenUtil
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.UserService
import com.fasterxml.jackson.module.kotlin.jsonMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(): OncePerRequestFilter() {

    @Autowired
    lateinit var businessService: BusinessService

    @Autowired
    lateinit var usersService: UserService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Get the auth header
        val header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header.isNullOrBlank() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return
        }

        // Get the jwt and validate if is valid
        val token = header.split(" ")[1].trim();
        val jwtTokenUtil = JwtTokenUtil(token)

        if (!jwtTokenUtil.validateToken()) {
            filterChain.doFilter(request, response);
            return
        }

        val userType: String = jwtTokenUtil.userTypeFromToken()
        val userName: String = jwtTokenUtil.usernameFromToken()

        val userDetails: UserDetails? = when (userType) {
            Business::class.toString() -> businessService.getBusinessByEmail(userName).orElse(null)
            User::class.toString() -> usersService.getUserByEmail(userName).orElse(null)
            else -> null
        }

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails!!.authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}