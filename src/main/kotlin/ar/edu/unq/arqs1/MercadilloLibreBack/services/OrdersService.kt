package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.LineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.order.OrdersRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product.ProductsRepository
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrdersService(val ordersRepository: OrdersRepository, val productsRepository: ProductsRepository) {
    fun addOrder(user: User): Order {
        return ordersRepository.findOne(Example.of(Order(buyer = user)))
            .orElseGet { ordersRepository.save(Order(buyer = user)) }
    }

    fun orders(buyer: User) =
        ordersRepository.findOrdersByBuyer(buyer)

    fun charge(order: Order): Result<Order> {
        return try {
            validateOrder(order)
            validateProducts(order)

            order.charge()
            removeStock(order.lineItems)
            Result.success(ordersRepository.save(order))
        } catch (e: Exception) {
            Result.failure(e)
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