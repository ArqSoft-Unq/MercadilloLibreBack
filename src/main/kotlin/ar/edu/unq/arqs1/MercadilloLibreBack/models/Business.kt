package ar.edu.unq.arqs1.MercadilloLibreBack.models

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import lombok.ToString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Table(name = "businesses")
@Entity
class Business (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @get:NotEmpty(message = "The name is required")
    val name: String? = null,

    @get:Email(message = "It must be a valid email")
    @get:NotEmpty(message = "The email is required")
    val email: String? = null,

    @get:NotEmpty(message = "The password is required")
    @Column(name = "encrypted_password")
    @JsonIgnore
    var encryptedPassword: String? = null): UserDetails {

    @JsonIgnore
    override fun getPassword(): String? = encryptedPassword

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("read"))

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