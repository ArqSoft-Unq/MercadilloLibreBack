package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.repositories.business.BusinessesRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BusinessService(private val businessesRepository: BusinessesRepository) {
    fun addBusiness(business: Business): Business = businessesRepository.save(business)
    fun getBusinessById(businessId: Long): Optional<Business> = businessesRepository.findById(businessId)
    fun getBusinessByEmail(email:String): Optional<Business> = businessesRepository.findBusinessByEmail(email)
}