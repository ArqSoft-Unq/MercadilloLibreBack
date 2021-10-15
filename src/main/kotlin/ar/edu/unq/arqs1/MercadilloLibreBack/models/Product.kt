package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Table(name = "products")
@Entity
@Data
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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

    @get:NotNull(message = "The seller is required")
    @OneToOne(optional = false)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    var seller: Business? = null,

    @Column(name="active")
    var isActive: Boolean = true
)