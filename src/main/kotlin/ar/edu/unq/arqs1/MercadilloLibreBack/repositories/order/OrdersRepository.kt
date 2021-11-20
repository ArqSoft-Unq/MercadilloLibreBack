package ar.edu.unq.arqs1.MercadilloLibreBack.repositories.order

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional
interface OrdersRepository : JpaRepository<Order, Long> {
    fun findOrdersByBuyer(buyer: User): List<Order>
}