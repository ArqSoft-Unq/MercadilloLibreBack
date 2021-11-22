package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.LineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.NewLineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.UpdateLineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.services.LineItemsService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/lineItems")
class LineItemsController {

    @Autowired
    lateinit var lineItemsService: LineItemsService

    @Autowired
    lateinit var productsService: ProductService

    @Autowired
    lateinit var ordersService: OrdersService

    @PostMapping
    fun create(@AuthenticationPrincipal user: User, @RequestBody @Valid newLineItem: NewLineItem): ResponseEntity<LineItem> {
        return ordersService.getOrder(newLineItem.orderId!!).map { order ->
            if(order.buyer.id != user.id) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                productsService.getProductById(newLineItem.productId!!).map { product ->
                    val result = lineItemsService.addLineItem(
                        LineItem(
                            price = product.price,
                            quantity = newLineItem.quantity!!,
                            product = product,
                            order = order
                        )
                    )

                    if(result.isSuccess) {
                        ResponseEntity.ok(result.getOrNull())
                    } else {
                        ResponseEntity(HttpStatus.BAD_REQUEST)
                    }
                }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
            }
        }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("/{id}")
    fun delete(@AuthenticationPrincipal user: User, @PathVariable(value="id") lineItemId: Long): ResponseEntity<Unit>? {
        return lineItemsService.findById(lineItemId).map { lineItem ->
            if (lineItem.order!!.buyer.id != user.id) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                val result = lineItemsService.deleteLineItem(lineItem)
                if(result.isSuccess) {
                    ResponseEntity.ok(Unit)
                } else {
                    ResponseEntity(HttpStatus.BAD_REQUEST)
                }
            }
        }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PutMapping
    fun changeQuantity(@AuthenticationPrincipal user: User, @RequestBody @Valid updateLineItem: UpdateLineItem): ResponseEntity<LineItem>? {
        return lineItemsService.findById(updateLineItem.id!!).map { lineItem ->
            if (lineItem.order!!.buyer.id != user.id) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                val result = lineItemsService.updateQuantity(lineItem, updateLineItem.quantity!!)
                if (result.isSuccess) {
                    ResponseEntity.ok(result.getOrNull())
                } else {
                    ResponseEntity(HttpStatus.BAD_REQUEST)
                }
            }
        }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }
}