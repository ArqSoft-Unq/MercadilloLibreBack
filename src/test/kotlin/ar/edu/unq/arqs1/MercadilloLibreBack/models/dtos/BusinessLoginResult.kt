package ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User


class BusinessLoginResult(
    val jwt: String,
    val entity: Business
)