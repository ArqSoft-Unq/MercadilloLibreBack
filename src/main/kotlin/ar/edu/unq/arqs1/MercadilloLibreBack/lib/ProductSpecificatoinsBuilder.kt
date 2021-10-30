package ar.edu.unq.arqs1.MercadilloLibreBack.lib

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import org.springframework.data.jpa.domain.Specification

class ProductSpecificationsBuilder(private val specification: Specification<Product>? = null) {
    private val params: MutableList<ProductSpecification> = arrayListOf()

    fun with(searchCriteria: SearchCriteria): ProductSpecificationsBuilder {
        params.add(ProductSpecification(searchCriteria))
        return this
    }

    fun build(): Specification<Product>? {
        if (params.isEmpty()) {
            return null
        }

        val startingValue: Specification<Product>?
        if (specification == null) {
            startingValue = Specification.where(params[0])
            params.remove(startingValue)
        } else {
            startingValue = specification
        }

        return params.fold(startingValue) { result, param -> result.and(param) }
    }

}