package ar.edu.unq.arqs1.MercadilloLibreBack.repositories.user

import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
@Transactional
interface UsersRepository : JpaRepository<User, Long> {
    fun findUserByEmail(email:String): Optional<User>
}