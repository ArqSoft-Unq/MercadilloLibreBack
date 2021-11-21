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

class CreateLineItemsTests: ApplicationTest(){
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

    @BeforeEach
    fun beforeEach() {
        business = businessService.addBusiness(Business(name = "name", email = "email@email", encryptedPassword = "lala"))
        product = productsService.addProduct(Product(name = "p1", description = "p 1", price = 10, stock = 1, seller = business!!, isActive = true))
        user = usersService.addUser(User(name = "name", lastname = "name", email = "email@email", encryptedPassword = "lala"))
        order = ordersService.addOrder(user!!)
    }

    @Test
    fun `creates a line item correctly`() {
        val lineItem = lineItemService.addLineItem(LineItem(price = 10, quantity = 1, order = order!!, product = product!!)).getOrNull()!!

        assertTrue(lineItemRepository.existsById(lineItem.id!!))
        assertEquals(order!!, lineItem.order)
        assertEquals(product!!, lineItem.product)
    }

    @Test
    fun `cant create a line item in a charged order`() {
        order!!.charge()
        val result = lineItemService.addLineItem(LineItem(price = 10, quantity = 1, order = order!!, product = product!!))

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Order.OrderCharged().message)
    }

    @Test
    fun `cant create a line item with a non-active product`() {
        product!!.isActive = false

        val result = lineItemService.addLineItem(LineItem(price = 10, quantity = 1, order = order!!, product = product!!))

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, LineItem.ProductDeactivated().message)
    }

    @Test
    fun `creating a line item takes the price from the product`() {
        val productPrice = product!!.price

        val result = lineItemService.addLineItem(LineItem(price = productPrice + 1000, quantity = 1, order = order!!, product = product!!))

        assertTrue(result.isSuccess)
        assertTrue(lineItemRepository.existsById(result.getOrNull()!!.id!!))
        assertEquals(productPrice, result.getOrNull()!!.price)
    }

    @Test
    fun `cant create a line item with quantity for more than available stock`() {
        val quantity = product!!.stock + 1
        val result = lineItemService.addLineItem(LineItem(price = 10, quantity = quantity, order = order!!, product = product!!))

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Product.MissingStock().message)
    }
}