package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Data
class NewUser (
    @get:NotEmpty(message = "The name is required")
    val name: String? = null,

    @get:NotEmpty(message = "The last name is required")
    val lastname: String? = null,

    @get:Email(message = "It must be a valid email")
    @get:NotEmpty(message = "The email is required")
    val email: String? = null,

    @get:NotEmpty(message = "The password is required")
    @Column(name = "password")
    val password: String? = null) {

    fun toUser(): User = User(name = name, email = email, lastname=lastname)
}