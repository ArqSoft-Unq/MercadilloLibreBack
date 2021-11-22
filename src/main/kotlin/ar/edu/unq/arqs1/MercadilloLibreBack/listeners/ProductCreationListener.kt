package ar.edu.unq.arqs1.MercadilloLibreBack.listeners

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

const val newProductsTopic: String = "new-products"

@Service
class ProductCreationListener {
    @KafkaListener(topics = [newProductsTopic], groupId = "group-id")
    fun listen(message: String) {
        println(message)
    }
}
