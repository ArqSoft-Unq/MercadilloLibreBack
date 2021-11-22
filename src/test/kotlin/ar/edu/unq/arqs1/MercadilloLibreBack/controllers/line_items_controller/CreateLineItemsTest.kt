package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.line_items_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Order
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.NewLineItem
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreateLineItemsTest: ApplicationTest() {
    lateinit var product: Product

    @Autowired
    lateinit var businessService: BusinessService

    @Autowired
    lateinit var productsService: ProductService

    lateinit var order: Order

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
    }

    @Test
    fun `create line item correctly`() {
        createLineItem(NewLineItem(quantity = 2, orderId = order.id, productId = product.id))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id", notNullValue()))
    }

    @Test
    fun `if the order is not from the user it fails with 404`() {
        credentials = createAUser("another@email")
        createLineItem(NewLineItem(quantity = 2, orderId = order.id, productId = product.id))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `if the product does not exists it fails with 404`() {
        createLineItem(NewLineItem(quantity = 2, orderId = order.id, productId = product.id!! + 1))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `if not enough stock fails with 400`() {
        createLineItem(NewLineItem(quantity = 10000, orderId = order.id, productId = product.id))
            .andExpect(status().isBadRequest)
    }
}