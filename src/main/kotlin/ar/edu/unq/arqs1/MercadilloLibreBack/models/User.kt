package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Table(name = "users")
@Entity
@Data
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @get:NotEmpty(message = "The name is required")
    val name: String? = null,

    @get:NotEmpty(message = "The last name is required")
    val lastname: String? = null,

    @get:Email(message = "It must be a valid email")
    @get:NotEmpty(message = "The email is required")
    @Column(unique = true)
    val email: String? = null,
)