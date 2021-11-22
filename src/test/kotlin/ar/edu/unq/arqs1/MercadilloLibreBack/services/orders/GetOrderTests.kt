package ar.edu.unq.arqs1.MercadilloLibreBack.services.orders

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.services.*
import org.hibernate.collection.internal.PersistentBag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class GetOrderTests : ApplicationTest() {
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

    lateinit var user: User
    lateinit var business: Business
    lateinit var product1: Product
    lateinit var product2: Product
    lateinit var order: Order
    var lineItems: List<LineItem> = emptyList()

    @BeforeEach
    fun beforeEach() {
        business =
            businessService.addBusiness(Business(name = "name", email = "email@email", encryptedPassword = "lala"))
        product1 = productsService.addProduct(
            Product(
                name = "p1",
                description = "p 1",
                price = 10,
                stock = 4,
                seller = business,
                isActive = true
            )
        )
        product2 = productsService.addProduct(
            Product(
                name = "p2",
                description = "p 2",
                price = 3,
                stock = 10,
                seller = business,
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
        order = ordersService.addOrder(user)
        lineItems = listOf(
            lineItemService.addLineItem(
                LineItem(
                    price = product1.price,
                    quantity = 1,
                    order = order,
                    product = product1
                )
            ).getOrThrow(),
            lineItemService.addLineItem(
                LineItem(
                    price = product2.price,
                    quantity = 2,
                    order = order,
                    product = product2
                )
            ).getOrThrow()
        )
    }

    @Test
    fun `get the order`() {
        val order = ordersService.getOrder(order.id!!).get()

        assertEquals(this.order.id, order.id)
        assertEquals(lineItems.sumOf { lineItem -> lineItem.itemPrice() }, order.total())
    }
}