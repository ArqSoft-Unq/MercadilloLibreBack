package ar.edu.unq.arqs1.MercadilloLibreBack

import ar.edu.unq.arqs1.MercadilloLibreBack.configuration.DatabaseCleanup
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewBusiness
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewUser
import ar.edu.unq.arqs1.MercadilloLibreBack.models.User
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.BusinessLoginResult
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.Credentials
import ar.edu.unq.arqs1.MercadilloLibreBack.models.dtos.UserLoginResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [ApplicationTest.ControllerTestConfig::class],
        //Here we can add properties only for testing
        properties = []
)
@ActiveProfiles(value = ["test"])
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

    protected fun <T, V> withAuthenticationExchange(
        url: String,
        method: HttpMethod,
        body: V?,
        responseType: Class<T>,
        jwt: String
    ): ResponseEntity<T> {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $jwt")
        val entity = HttpEntity<V>(body, headers)

        return restTemplate.exchange(url, method, entity, responseType)
    }

    protected fun <T, V> userAuthenticatedExchange(
        credentials: Credentials,
        url: String,
        method: HttpMethod,
        body: V?,
        responseType: Class<T>
    ): ResponseEntity<T> = withAuthenticationExchange(url, method, body, responseType, loginUser(credentials))

    protected fun <T, V> businessAuthenticatedExchange(
        credentials: Credentials,
        url: String,
        method: HttpMethod,
        body: V?,
        responseType: Class<T>
    ): ResponseEntity<T> = withAuthenticationExchange(url, method, body, responseType, loginBusiness(credentials))
}
