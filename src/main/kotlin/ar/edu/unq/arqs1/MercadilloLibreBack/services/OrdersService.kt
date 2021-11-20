package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.order.OrdersRepository
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service

@Service
class OrdersService(val ordersRepository: OrdersRepository) {
    fun addOrder(user: User): Order {
        return ordersRepository.findOne(Example.of(Order(buyer = user)))
            .orElseGet { ordersRepository.save(Order(buyer = user)) }
    }

    fun orders(buyer: User) =
        ordersRepository.findOrdersByBuyer(buyer)
}