package ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos

import ar.edu.unq.arqs1.MercadilloLibreBack.models.User


class UserLoginResult(
    val jwt: String,
    val entity: User
)