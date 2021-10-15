package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.validation.constraints.Min

@Data
class UpdateProduct (
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,

    @get:Min(0)
    var price: Int? = null,

    @get:Min(0)
    var stock: Int? = null,
)