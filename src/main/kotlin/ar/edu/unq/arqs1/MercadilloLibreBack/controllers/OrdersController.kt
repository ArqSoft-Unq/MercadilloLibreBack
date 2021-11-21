package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/orders")
@Validated
class OrdersController(
    private val ordersService: OrdersService) {

    @PostMapping
    fun createOrder(@AuthenticationPrincipal user: User): ResponseEntity<Order> {
        return ResponseEntity.ok(ordersService.addOrder(user))
    }

    @GetMapping
    fun getOrders(@AuthenticationPrincipal user: User): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(ordersService.orders(user))
    }

    @PutMapping("/{id}")
    fun chargeOrder(@AuthenticationPrincipal user: User, @PathVariable(value="id") orderId: Long): ResponseEntity<Order> {
        return ordersService.getOrder(orderId).map { order ->
            if(order.buyer.id != user.id) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                val result = ordersService.charge(order)
                if(result.isSuccess) {
                    ResponseEntity.ok(result.getOrNull())
                } else {
                    ResponseEntity(HttpStatus.BAD_REQUEST)
                }
            }
        }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

}
