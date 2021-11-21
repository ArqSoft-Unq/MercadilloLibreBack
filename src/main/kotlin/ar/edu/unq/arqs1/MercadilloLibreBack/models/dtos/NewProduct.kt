package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Data
class NewProduct (
    @get:NotEmpty(message = "The name is required")
    var name: String? = null,

    @get:NotEmpty(message = "The description is required")
    var description: String? = null,

    @get:NotNull(message = "The price is required")
    @get:Min(0)
    var price: Int? = null,

    @get:NotNull(message = "The stock is required")
    @get:Min(0)
    var stock: Int? = null,
) {
    fun toProduct(seller: Business): Product =
        Product(name = name!!, description = description!!, price = price!!, stock = stock!!, seller = seller)
}