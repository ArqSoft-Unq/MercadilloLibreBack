package ar.edu.unq.arqs1.MercadilloLibreBack

import ar.edu.unq.arqs1.MercadilloLibreBack.configuration.DatabaseCleanup
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
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
}
