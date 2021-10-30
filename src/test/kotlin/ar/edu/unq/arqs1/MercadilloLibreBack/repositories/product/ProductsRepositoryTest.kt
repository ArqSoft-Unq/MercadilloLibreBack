package ar.edu.unq.arqs1.MercadilloLibreBack.repositories.product

import ar.edu.unq.arqs1.MercadilloLibreBack.lib.ProductSpecification
import ar.edu.unq.arqs1.MercadilloLibreBack.lib.SearchCriteria
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business.BusinessesRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.domain.Specification


@DataJpaTest
class ProductsRepositoryTest {
    @Autowired
    lateinit var repository: ProductsRepository

    @Autowired
    lateinit var businessRepository: BusinessesRepository

    private var book: Product? = null
    private var oboe: Product? = null
    private var seller: Business? = null

    @BeforeEach
    fun setup() {
        seller = businessRepository.save(Business(email = "asas@as.co", name = "las", encryptedPassword = "asd"))
        book = repository.save(
            Product(
                name = "my book",
                description = "nice book",
                price = 10,
                stock = 10,
                seller = seller
            )
        )
        oboe = repository.save(
            Product(
                name = "my oboe",
                description = "nice oboe",
                price = 100,
                stock = 2,
                seller = seller
            )
        )
    }

    @Test
    fun `get products by similar name`() {
        val spec = ProductSpecification(SearchCriteria("name", ":", "my"))
        val results: List<Product> = repository.findAll(spec)
        assertThat(results).hasSize(2)
        assertThat(book).isIn(results)
        assertThat(oboe).isIn(results)
    }

    @Test
    fun `search by two clauses`() {
        val spec1 = ProductSpecification(SearchCriteria("name", ":", "book"))
        val spec2 = ProductSpecification(SearchCriteria("name", ":", "oboe"))
        val results: List<Product> = repository.findAll(Specification.where(spec1).or(spec2))
        assertThat(results).hasSize(2)
        assertThat(book).isIn(results)
        assertThat(oboe).isIn(results)
    }

    @Test
    fun `combine search criteria`() {
        val spec1 = ProductSpecification(SearchCriteria("name", ":", "my"))
        val spec2 = ProductSpecification(SearchCriteria("stock", ">", 5))
        val results: List<Product> = repository.findAll(Specification.where(spec1).and(spec2))
        assertThat(results).hasSize(1)
        assertThat(book).isIn(results)
    }

    @Test
    fun `search by seller`() {
        val spec1 = ProductSpecification(SearchCriteria("seller", ":", seller!!))
        val results: List<Product> = repository.findAll(spec1)
        assertThat(results).hasSize(2)
        assertThat(book).isIn(results)
        assertThat(oboe).isIn(results)
    }
}