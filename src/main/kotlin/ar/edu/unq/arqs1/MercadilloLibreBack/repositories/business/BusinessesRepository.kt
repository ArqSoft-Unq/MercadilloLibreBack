package ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface BusinessesRepository : JpaRepository<Business, Long>{
    fun findBusinessByEmail(email:String): Optional<Business>
}