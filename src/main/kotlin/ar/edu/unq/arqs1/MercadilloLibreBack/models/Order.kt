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
    var  buyer: User,

    var  status: String = Status.PENDING.code,

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var lineItems: List<LineItem> = emptyList()
){
    fun charge() {
        status = Status.valueOf(status).next().code
    }

    fun isCharged(): Boolean {
        return Status.valueOf(status) == Status.CHARGED
    }

    fun isEmpty(): Boolean {
        return lineItems.isEmpty()
    }

    enum class Status(val code: String) {
        PENDING("PENDING") {
            override fun next() = CHARGED
        },
        CHARGED("CHARGED") {
            override fun next() = CHARGED
        };

        abstract fun next(): Status
    }

    class OrderCharged: Exception("The order is already charged")
    class EmptyOrder: Exception("The order is empty")
}
