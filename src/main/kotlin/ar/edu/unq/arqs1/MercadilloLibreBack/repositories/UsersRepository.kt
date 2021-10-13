package ar.edu.unq.arqs1.MercadilloLibreBack.repositories

import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface UsersRepository : JpaRepository<User, Long>