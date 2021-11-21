package ar.edu.unq.arqs1.MercadilloLibreBack.services.orders

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.services.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ChargeOrderTest : ApplicationTest() {
    @Autowired
    lateinit var usersService: UserService

    @Autowired
    lateinit var businessService: BusinessService

    @Autowired
    lateinit var productsService: ProductService

    @Autowired
    lateinit var ordersService: OrdersService

    @Autowired
    lateinit var lineItemService: LineItemsService

    var user: User? = null
    var business: Business? = null
    var product: Product? = null
    var order: Order? = null
    var lineItems: List<LineItem> = emptyList()

    @BeforeEach
    fun beforeEach() {
        business =
            businessService.addBusiness(Business(name = "name", email = "email@email", encryptedPassword = "lala"))
        product = productsService.addProduct(
            Product(
                name = "p1",
                description = "p 1",
                price = 10,
                stock = 1,
                seller = business!!,
                isActive = true
            )
        )
        user = usersService.addUser(
            User(
                name = "name",
                lastname = "name",
                email = "email@email",
                encryptedPassword = "lala"
            )
        )
        order = ordersService.addOrder(user!!)
        lineItems = listOf(
            lineItemService.addLineItem(
                LineItem(
                    price = product!!.price,
                    quantity = product!!.stock,
                    order = order!!,
                    product = product!!
                )
            ).getOrThrow()
        )
        order!!.lineItems = lineItems
    }

    @Test
    fun `charge order correctly`() {
        val result = ordersService.charge(order!!)

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()!!.status, Order.Status.CHARGED.code)
    }

    @Test
    fun `updates the product stock`() {
        val previousStock = product!!.stock
        val result = ordersService.charge(order!!)

        assertTrue(result.isSuccess)
        assertEquals(previousStock - lineItems.sumOf { lineItem -> lineItem.quantity }, product!!.stock)
    }

    @Test
    fun `cant charge empty order`() {
        order!!.lineItems = emptyList()
        val result = ordersService.charge(order!!)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Order.EmptyOrder().message)
    }

    @Test
    fun `cant charge charged order`() {
        ordersService.charge(order!!)
        val result = ordersService.charge(order!!)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Order.OrderCharged().message)
    }

    @Test
    fun `cant charge if one of the products doesnt have enough stock`() {
        lineItems[0].quantity = product!!.stock + 1
        val result = ordersService.charge(order!!)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Product.MissingStock().message)
    }

    @Test
    fun `cant charge if one of the products is not active`() {
        product!!.isActive = false
        val result = ordersService.charge(order!!)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, LineItem.ProductDeactivated().message)
    }
}