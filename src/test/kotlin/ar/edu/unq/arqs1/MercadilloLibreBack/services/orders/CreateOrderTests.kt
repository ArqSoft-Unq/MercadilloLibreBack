package ar.edu.unq.arqs1.MercadilloLibreBack.services.orders

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.order.OrdersRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateOrderTests: ApplicationTest(){
    @Autowired
    lateinit var usersService: UserService

    @Autowired
    lateinit var ordersService: OrdersService

    @Autowired
    lateinit var ordersRepository: OrdersRepository

    var user: User? = null

    @BeforeEach
    fun beforeEach() {
        user = usersService.addUser(User(name = "name", lastname = "name", email = "email@email", encryptedPassword = "lala"))
    }

    @Test
    fun `creates the order correctly`() {
        var newOrder = ordersService.addOrder(user!!)

        assertTrue(ordersRepository.existsById(newOrder.id!!))
        assertEquals(Order.Status.PENDING.code, newOrder.status)
        assertEquals(user!!, newOrder.buyer)
    }

    @Test
    fun `when there is a pending order for the user it does not creates a new one`() {
        val oldPendingOrder = ordersService.addOrder(user!!)
        val ordersQuantity = ordersRepository.count()

        val newPendingOrder = ordersService.addOrder(user!!)

        assertEquals(ordersQuantity, ordersRepository.count())
        assertEquals(oldPendingOrder.id, newPendingOrder.id)
        assertEquals(Order.Status.PENDING.code, newPendingOrder.status)
    }
}