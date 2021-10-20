package ar.edu.unq.arqs1.MercadilloLibreBack.repositories

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface BusinessesRepository : JpaRepository<Business, Long>{

    @Query("SELECT b FROM Business b  WHERE b.email = ?1 AND b.password = ?2")
    fun findByEmailAndPassword( email:String,password:String): Optional<Business>
}