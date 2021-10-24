package ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos

import lombok.Data
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Data
class Credentials (
    @get:Email(message = "It must be a valid email")
    @get:NotEmpty(message = "The email is required")
    val email: String? = null,

    @get:NotEmpty(message = "The password is required")
    @Column(name = "password")
    val password: String? = null)