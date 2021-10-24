package ar.edu.unq.arqs1.MercadilloLibreBack.security

import ar.edu.unq.arqs1.MercadilloLibreBack.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService : UserDetailsService {
    @Autowired
    lateinit var userService: UserService

    override fun loadUserByUsername(username: String): UserDetails =
        userService.getUserByEmail(username).orElseThrow { UsernameNotFoundException("Business not present") }
}