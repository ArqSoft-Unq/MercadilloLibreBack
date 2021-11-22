package ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class UpdateLineItem(
    @get:NotNull(message = "The quantity is required")
    @get:Min(1)
    var quantity: Int?,

    @get:NotNull(message = "The order is required")
    var id: Long?,
)
