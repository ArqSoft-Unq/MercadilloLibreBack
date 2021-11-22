package ar.edu.unq.arqs1.MercadilloLibreBack.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Data
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Table(name = "line_items")
@Entity
@Data
class LineItem (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get:NotNull(message = "The price is required")
    @get:Min(0)
    var price: Int,

    @get:NotNull(message = "The quantity is required")
    @get:Min(1)
    var quantity: Int,

    @get:NotNull(message = "The order is required")
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    var  order: Order?,

    @get:NotNull(message = "The product is required")
    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    var  product: Product
    ) {

    @JsonProperty("itemPrice")
    fun itemPrice(): Int {
        return price * quantity
    }

    class ProductDeactivated: Exception("The product is deactivated")
}
