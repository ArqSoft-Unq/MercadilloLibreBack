package ar.edu.unq.arqs1.MercadilloLibreBack.services.line_items

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.line_item.LineItemRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.services.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DeleteLineItemsTests: ApplicationTest(){
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
        product = productsService.addProduct(Product(name = "p1", description = "p 1", price = 10, stock = 1, seller = business!!, isActive = true))
        user = usersService.addUser(User(name = "name", lastname = "name", email = "email@email", encryptedPassword = "lala"))
        order = ordersService.addOrder(user!!)
        lineItem = lineItemService.addLineItem(LineItem(price = 10, quantity = 1, order = order!!, product = product!!)).getOrNull()!!
    }

    @Test
    fun `deletes a line item correctly`() {
        val result = lineItemService.deleteLineItem(lineItem!!)

        assertTrue(result.isSuccess)
        assertFalse(lineItemRepository.existsById(lineItem!!.id!!))
    }

    @Test
    fun `cant delete a line item in a charged order`() {
        order!!.charge()
        val result = lineItemService.deleteLineItem(lineItem!!)

        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull()!!.message, Order.OrderCharged().message)
    }
}