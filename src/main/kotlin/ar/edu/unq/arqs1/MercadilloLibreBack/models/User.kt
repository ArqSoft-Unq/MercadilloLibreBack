package ar.edu.unq.arqs1.MercadilloLibreBack.models

import ar.edu.unq.arqs1.MercadilloLibreBack.configuration.security.WebSecurity.Companion.USER_AUTHORITY
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
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

    @get:NotEmpty(message = "The password is required")
    @Column(name = "encrypted_password")
    @JsonIgnore
    var encryptedPassword: String? = null) : UserDetails {

    @JsonIgnore
    override fun getPassword(): String? = encryptedPassword

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(USER_AUTHORITY))

    @JsonIgnore
    override fun getUsername(): String? = email

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = false

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = false

    @JsonIgnore
    override fun isEnabled(): Boolean = true
}