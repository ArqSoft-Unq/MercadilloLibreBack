package ar.edu.unq.arqs1.MercadilloLibreBack

import ar.edu.unq.arqs1.MercadilloLibreBack.configuration.DatabaseCleanup
import ar.edu.unq.arqs1.MercadilloLibreBack.models.*
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.BusinessLoginResult
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.UserLoginResult
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [ApplicationTest.ControllerTestConfig::class],
        //Here we can add properties only for testing
        properties = []
)
@ActiveProfiles(value = ["test"])
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    @LocalServerPort
    var serverPort: Int = 0

    @TestConfiguration
    internal class ControllerTestConfig {
        //Here we should add all the beans that we'll need only for testing
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    var credentials: Credentials? = null

    @AfterEach
    fun cleanupDatabase() {
        databaseCleanup.truncate()
    }

    fun loginUser(credentials: Credentials): String {
        val result = restTemplate.postForEntity("/v1/users/login", credentials, UserLoginResult::class.java )
        assert(result.statusCode == HttpStatus.OK)
        return result.body!!.jwt
    }

    fun loginBusiness(credentials: Credentials): String =
        restTemplate.postForEntity("/v1/businesses/login", credentials, BusinessLoginResult::class.java ).body!!.jwt

    fun createUser(newUser: NewUser): User {
        val result = restTemplate.postForEntity("/v1/users", newUser, User::class.java)
        assert(result.statusCode == HttpStatus.OK)
        return result.body!!
    }

    fun createAUser(): Credentials {
        val newUser = NewUser(name = "name", lastname = "lala", email = "email@email.com", password = "sarlanga")
        val credentials = Credentials(newUser.email, newUser.password)
        this.createUser(newUser)
        return credentials
    }

    fun createBusiness(newBusiness: NewBusiness) =
        restTemplate.postForEntity("/v1/businesses", newBusiness, Business::class.java).body!!

    protected fun <V> withAuthenticationExchange(
        url: String,
        method: HttpMethod,
        body: V?,
        jwt: String
    ): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.request(method, url)
                .header("Authorization", "Bearer $jwt")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        )
    }

    protected fun <V> exchange(
        url: String,
        method: HttpMethod,
        body: V?
    ): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.request(method, url)
                .content(objectMapper.writeValueAsString(body))
        )
    }

    protected fun <V> userAuthenticatedExchange(
        credentials: Credentials,
        url: String,
        method: HttpMethod,
        body: V?
    ): ResultActions = withAuthenticationExchange(url, method, body, loginUser(credentials))

    protected fun <V> businessAuthenticatedExchange(
        credentials: Credentials,
        url: String,
        method: HttpMethod,
        body: V?,
    ): ResultActions = withAuthenticationExchange(url, method, body, loginBusiness(credentials))

    fun postOrder(authenticated: Boolean): ResultActions =
        if (authenticated) {
            userAuthenticatedExchange(credentials!!, "/v1/orders", HttpMethod.POST, null)
        } else {
            exchange("/v1/orders", HttpMethod.POST, null)
        }

    fun createOrder(credentials: Credentials): Order {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer ${loginUser(credentials)}")
        val entity = HttpEntity<Order>(null, headers)

        return restTemplate.exchange("/v1/orders", HttpMethod.POST, entity, Order::class.java).body!!
}
}
