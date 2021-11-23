package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.configuration.orderChargedTopic
import ar.edu.unq.arqs1.MercadilloLibreBack.models.LineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.order.OrdersRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.Lock
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.LockModeType

@Service
@Transactional
class OrdersService(
    val ordersRepository: OrdersRepository,
    val productsRepository: ProductsRepository,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun addOrder(user: User): Order {
        return ordersRepository.findOne(Example.of(Order(buyer = user)))
            .orElseGet { ordersRepository.save(Order(buyer = user)) }
    }

    fun orders(buyer: User) =
        ordersRepository.findOrdersByBuyer(buyer)

    @Lock(LockModeType.PESSIMISTIC_READ)
    fun charge(order: Order): Result<Order> {
        return try {
            validateOrder(order)
            validateProducts(order)

            order.charge()
            removeStock(order.lineItems)
            val chargedOrder = ordersRepository.save(order)
            registerEvent(chargedOrder)
            Result.success(chargedOrder)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun registerEvent(chargedOrder: Order) {
        kafkaTemplate.send(orderChargedTopic, OrderChargedMessage(chargedOrder).toJson())
    }

    class OrderChargedMessage(val order: Order) {
        fun toJson(): String {
            return ObjectMapper().writeValueAsString(mapOf(
              "orderId" to order.id,
                "total" to order.total(),
                "buyerId" to order.buyer.id
            ))
        }
    }

    fun getOrder(id: Long): Optional<Order> {
        return Optional.of(ordersRepository.getById(id))
    }

    private fun removeStock(lineItems: List<LineItem>) {
        productsRepository.saveAll(lineItems.map { lineItem -> lineItem.product.removeStock(lineItem.quantity); lineItem.product })
    }

    private fun validateProducts(order: Order) {
        if (order.lineItems.map { lineItem -> lineItem.product.canSupplyStockFor(lineItem.quantity) }.any { !it }) {
            throw Product.MissingStock()
        }
        if (order.lineItems.map { lineItem -> lineItem.product.isActive }.any { !it }) {
            throw LineItem.ProductDeactivated()
        }
    }

    private fun validateOrder(order: Order) {
        if (order.isEmpty()) {
            throw Order.EmptyOrder()
        }

        if (order.isCharged()) {
            throw Order.OrderCharged()
        }
    }
}