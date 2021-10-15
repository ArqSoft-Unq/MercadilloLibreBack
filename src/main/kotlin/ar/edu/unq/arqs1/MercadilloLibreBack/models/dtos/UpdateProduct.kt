package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Data
class UpdateProduct (
    var id: Long? = null,

    @get:Size(min = 1)
    var name: String? = null,

    @get:Size(min = 1)
    var description: String? = null,

    @get:Min(0)
    var price: Int? = null,

    @get:Min(0)
    var stock: Int? = null,
)