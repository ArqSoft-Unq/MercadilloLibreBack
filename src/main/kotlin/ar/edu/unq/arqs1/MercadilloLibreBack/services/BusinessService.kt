package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.BusinessesRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BusinessService(private val businessesRepository: BusinessesRepository) {
    fun addBusiness(business: Business): ResponseEntity<Business> =
            ResponseEntity.ok(businessesRepository.save(business))

    fun getBusinessById(businessId: Long): ResponseEntity<Business> =
            businessesRepository.findById(businessId).map { businesses -> ResponseEntity.ok(businesses) }
                    .orElse(ResponseEntity.notFound().build())
}