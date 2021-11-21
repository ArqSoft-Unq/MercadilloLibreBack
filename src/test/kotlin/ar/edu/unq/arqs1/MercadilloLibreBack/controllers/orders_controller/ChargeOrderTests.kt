package ar.edu.unq.arqs1.MercadilloLibreBack.controllers.users_controller

import ar.edu.unq.arqs1.MercadilloLibreBack.ApplicationTest
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.services.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ChargeOrderTests : ApplicationTest() {

    @Autowired
    lateinit var businessService: BusinessService

    @Autowired
    lateinit var productsService: ProductService

    @Autowired
    lateinit var lineItemService: LineItemsService

    var order: Order? = null

    fun chargeOrder(order: Order): ResultActions =
        userAuthenticatedExchange(credentials!!, "/v1/orders/${order.id}", HttpMethod.PUT, order)

    @BeforeEach
    fun beforeEach() {
        credentials = createAUser()
        order = createOrder(credentials!!)

        val business =
            businessService.addBusiness(Business(name = "name", email = "email@email", encryptedPassword = "lala"))

        val product = productsService.addProduct(
            Product(
                name = "p1",
                description = "p 1",
                price = 10,
                stock = 1,
                seller = business,
                isActive = true
            )
        )
        lineItemService.addLineItem(
            LineItem(
                price = product.price,
                quantity = product.stock,
                order = order!!,
                product = product
            )
        ).getOrThrow()
    }

    @Test
    fun `charges the order`() {
        order!!.charge()
        chargeOrder(order!!)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id", `is`(order!!.id!!.toInt())))
            .andExpect(jsonPath("$.status", `is`(Order.Status.CHARGED.code)))
    }

    @Test
    fun `if its empty fails with 400`() {
        chargeOrder(order!!)
        val order = createOrder(credentials!!)
        order.charge()
        chargeOrder(order)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `if the order is from another user it fails with a 404`() {
        credentials = Credentials(email = "li@lo.lu", password = "p")
        createUser(NewUser(name = "la", lastname = "le", email = credentials!!.email, password = credentials!!.password))

        order!!.charge()
        chargeOrder(order!!)
            .andExpect(status().isNotFound)
    }
}

