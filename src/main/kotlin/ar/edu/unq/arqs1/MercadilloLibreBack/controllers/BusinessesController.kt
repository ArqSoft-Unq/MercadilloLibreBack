package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/businesses")
@Validated
class BusinessesController(private val businessService: BusinessService) {
    @PostMapping
    fun addUser(@RequestBody @Valid business: Business): ResponseEntity<Business> =
        ResponseEntity.ok(businessService.addBusiness(business))

    @GetMapping("/{id}")
    fun getUser(@PathVariable(value = "id") businessId: Long): ResponseEntity<Business> =
        businessService.getBusinessById(businessId).map { businesses -> ResponseEntity.ok(businesses) }
            .orElse(ResponseEntity.notFound().build())
}