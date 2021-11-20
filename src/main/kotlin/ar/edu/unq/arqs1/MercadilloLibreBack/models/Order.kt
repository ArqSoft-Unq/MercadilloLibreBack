package ar.edu.unq.arqs1.MercadilloLibreBack.models

import lombok.Data
import javax.persistence.*
import javax.validation.constraints.NotNull

@Table(name = "orders")
@Entity
@Data
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get:NotNull(message = "The user is required")
    @OneToOne(optional = false)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    var  buyer: User? = null,

    var  status: String = Status.PENDING.code
){
    enum class Status(val code: String) {
        PENDING("PENDING") {
            override fun next() = CHARGED
        },
        CHARGED("CHARGED") {
            override fun next() = CHARGED
        };

        abstract fun next(): Status
    }
}
