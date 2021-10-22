package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Data
class NewBusiness (
    @get:NotEmpty(message = "The name is required")
    val name: String? = null,

    @get:Email(message = "It must be a valid email")
    @get:NotEmpty(message = "The email is required")
    val email: String? = null,

    @get:NotEmpty(message = "The password is required")
    @Column(name = "password")
    val password: String? = null) {

    fun toBusiness(): Business = Business(name = name, email = email)
}