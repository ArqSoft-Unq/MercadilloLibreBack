package ar.edu.unq.arqs1.MercadilloLibreBack.services.line_items

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.line_item.LineItemRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.services.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ChangeLineItemQuantity: ApplicationTest() {
    @Autowired
    lateinit var usersService: UserService

    @Autowired
    lateinit var businessService: BusinessService

    @Autowired
    lateinit var productsService: ProductService

    @Autowired
    lateinit var ordersService: OrdersService

    @Autowired
    lateinit var lineItemRepository: LineItemRepository

    @Autowired
    lateinit var lineItemService: LineItemsService

    var user: User? = null
    var business: Business? = null
    var product: Product? = null
    var order: Order? = null
    var lineItem: LineItem? = null

    @BeforeEach
    fun beforeEach() {
        business = businessService.addBusiness(Business(name = "name", email = "email@email", encryptedPassword = "lala"))
        product = productsService.addProduct(Product(name = "p1", description = "p 1", price = 10, stock = 100, seller = business!!, isActive = true))
        user = usersService.addUser(User(name = "name", lastname = "name", email = "email@email", encryptedPassword = "lala"))
        order = ordersService.addOrder(user!!)
        lineItem = lineItemService.addLineItem(LineItem(price = 10, quantity = 1, order = order!!, product = product!!)).getOrNull()!!
    }

    @Test
    fun `changes the line item quantity`() {
        val quantity = product!!.stock - 10
        val result = lineItemService.updateQuantity(lineItem!!, quantity)

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()!!.quantity, quantity)
    }

    @Test
    fun `cant change quantity in a charged order`() {
        order!!.charge()
        val result = lineItemService.updateQuantity(lineItem!!, 10)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Order.OrderCharged().message)
    }

    @Test
    fun `cant change quantity for more than available stock`() {
        val quantity = product!!.stock + 1
        val result = lineItemService.updateQuantity(lineItem!!, quantity)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Product.MissingStock().message)
    }
}