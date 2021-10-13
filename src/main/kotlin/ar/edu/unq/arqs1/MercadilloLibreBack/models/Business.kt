package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Table(name = "businesses")
@Entity
@Data
class Business (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @get:NotEmpty(message = "The name is required")
    val name: String? = null,

    @get:Email(message = "It must be a valid email")
    @get:NotEmpty(message = "The email is required")
    val email: String? = null,
)