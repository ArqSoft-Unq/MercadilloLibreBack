package ar.edu.unq.arqs1.MercadilloLibreBack.lib

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.*

class ProductSpecification(private val criteria: SearchCriteria) : Specification<Product> {
    override fun toPredicate(
        root: Root<Product>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): Predicate? {
        if (criteria.operation.equals(other = ">", ignoreCase = true)) {
            return builder.greaterThan(root.get(criteria.key), criteria.value.toString())
        } else if (criteria.operation.equals(other = "<", ignoreCase = true)) {
            return builder.lessThan(root.get(criteria.key), criteria.value.toString())
        } else if (criteria.operation.equals(other = ":", ignoreCase = true)) {
            return tryCastToString(root.get<Any>(criteria.key)) { path ->
                builder.like(path, "%${criteria.value}%")
            }.orElse(builder.equal(root.get<Any>(criteria.key), criteria.value))
        }
        return null
    }

    private inline fun tryCastToString(instance: Path<*>, block: (Path<String>) -> Predicate): Optional<Predicate> {
        return if (instance.javaType.equals(String::class.java)) {
            Optional.of(block(instance as Path<String>))
        } else {
            Optional.empty()
        }
    }
}