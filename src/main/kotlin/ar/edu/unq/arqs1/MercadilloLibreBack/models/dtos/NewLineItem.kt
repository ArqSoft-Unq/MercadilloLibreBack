package ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class NewLineItem(
    @get:NotNull(message = "The quantity is required")
    @get:Min(1)
    var quantity: Int?,

    @get:NotNull(message = "The order is required")
    var orderId: Long?,

    @get:NotNull(message = "The product is required")
    var productId: Long?,
    )
