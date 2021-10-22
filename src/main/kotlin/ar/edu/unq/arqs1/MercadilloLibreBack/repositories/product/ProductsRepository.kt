package ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface ProductsRepository : JpaRepository<Product, Long>