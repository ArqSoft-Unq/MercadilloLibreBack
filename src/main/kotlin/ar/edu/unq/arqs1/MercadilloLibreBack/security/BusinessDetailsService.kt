package ar.edu.unq.arqs1.MercadilloLibreBack.security

import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class BusinessDetailsService : UserDetailsService {
    @Autowired
    lateinit var businessService: BusinessService

    override fun loadUserByUsername(username: String): UserDetails = businessService.getBusinessByEmail(username)
        .orElseThrow { UsernameNotFoundException("Business not present") }
}