package ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos

import org.springframework.security.core.userdetails.UserDetails

class LoginResult(
    val jwt: String,
    val entity: UserDetails
)