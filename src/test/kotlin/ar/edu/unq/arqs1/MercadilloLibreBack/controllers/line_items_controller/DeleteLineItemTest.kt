package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.line_items_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.LineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.line_item.LineItemRepository
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.LineItemsService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.OrdersService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DeleteLineItemTest: ApplicationTest() {

    lateinit var product: Product

    fun deleteLineItem(id: Long): ResultActions =
        userAuthenticatedExchange(credentials!!, "/v1/lineItems/${id}", HttpMethod.DELETE, null)

    @Autowired
    lateinit var businessService: BusinessService

    @Autowired
    lateinit var productsService: ProductService

    @Autowired
    lateinit var lineItemRepository: LineItemRepository

    @Autowired
    lateinit var lineItemService: LineItemsService

    @Autowired
    lateinit var orderService: OrdersService

    lateinit var order: Order

    lateinit var lineItem: LineItem

    @BeforeEach
    fun setup() {
        credentials = createAUser()
        order = createOrder(credentials!!)
        val business =
            businessService.addBusiness(Business(name = "name", email = "email@email", encryptedPassword = "lala"))
        product = productsService.addProduct(
            Product(
                name = "p1",
                description = "p 1",
                price = 10,
                stock = 10,
                seller = business,
                isActive = true
            )
        )
        lineItem = lineItemService.addLineItem(LineItem(price = 10, quantity = 1, product = product, order = order)).getOrThrow()
        order.lineItems = listOf(lineItem)
    }

    @Test
    fun `delete line item correctly`() {
        deleteLineItem(lineItem.id!!)
            .andExpect(status().isOk)
        assertFalse(lineItemRepository.existsById(lineItem.id!!))
    }

    @Test
    fun `if the order is not of the user it returns a 404`() {
        credentials = createAUser("another@email")
        deleteLineItem(lineItem.id!!)
            .andExpect(status().isNotFound)
    }

    @Test
    fun `if line item does not exists it returns a 404`() {
        deleteLineItem(lineItem.id!! + 1)
            .andExpect(status().isNotFound)
    }

    @Test
    fun `if order is charged it returns a 400`() {
        orderService.charge(order).getOrThrow()
        deleteLineItem(lineItem.id!!)
            .andExpect(status().isBadRequest)
    }

}