package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.ProductSpecification
import ar.edu.unq.arqs1.MercadilloLibreBack.lib.ProductSpecificationsBuilder
import ar.edu.unq.arqs1.MercadilloLibreBack.lib.SearchCriteria
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.ProductsResponse
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.regex.Pattern
import javax.validation.Valid

@RestController
@RequestMapping("/v1/orders")
@Validated
class OrdersController(
    private val productsService: ProductService,
    private val ordersService: OrdersService) {

    @PostMapping
    fun createOrder(@AuthenticationPrincipal user: User): ResponseEntity<Order> {
        return ResponseEntity.ok(ordersService.addOrder(user))
    }

    @GetMapping
    fun getOrders(@AuthenticationPrincipal user: User): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(ordersService.orders(user))
    }

}
