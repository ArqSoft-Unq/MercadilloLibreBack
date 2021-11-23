package ar.edu.unq.arqs1.MercadilloLibreBack.listeners

import ar.edu.unq.arqs1.MercadilloLibreBack.models.Product
import ar.edu.unq.arqs1.MercadilloLibreBack.services.BusinessService
import ar.edu.unq.arqs1.MercadilloLibreBack.services.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

const val newProductsTopic: String = "new-products"

@Service
class ProductCreationListener(val productsService: ProductService, val businessService: BusinessService) {
    class Value : HashMap<String, String>()

    @KafkaListener(topics = [newProductsTopic], groupId = "group-id")
    fun listen(message: String) {
        val value: Map<String, String> = ObjectMapper().readValue(message, Value::class.java)
        if (value["error"] != null) {
            processError(message)
        } else {
            processSuccess(value)
        }
    }

    private fun processSuccess(value: Map<String, String>) {
        productsService.addProduct(
            Product(
                name = value["name"]!!,
                description = value["description"]!!,
                price = value["price"]!!.toInt(),
                stock = value["stock"]!!.toInt(),
                seller = businessService.getBusinessById(value["sellerId"]!!.toLong()).get()
            )
        )
    }

    private fun processError(message: String) {
        println(message)
    }
}
