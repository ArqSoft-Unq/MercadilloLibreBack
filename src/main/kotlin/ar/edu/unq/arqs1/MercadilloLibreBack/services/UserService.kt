package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.user.UsersRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val usersRepository: UsersRepository) {
    fun addUser(user: User): ResponseEntity<User> =
            ResponseEntity.ok(usersRepository.save(user))

    fun getUserById(userId: Long): ResponseEntity<User> =
            usersRepository.findById(userId).map { user -> ResponseEntity.ok(user) }
                    .orElse(ResponseEntity.notFound().build())

    fun getUserByEmail(email: String): Optional<User> =
        usersRepository.findUserByEmail(email)
}