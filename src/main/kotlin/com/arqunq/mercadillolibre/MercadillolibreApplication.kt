package com.arqunq.mercadillolibre

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class MercadillolibreApplication

fun main(args: Array<String>) {
	runApplication<MercadillolibreApplication>(*args)
}

@RestController
class VersionController {
    @GetMapping("/version")
    fun index(): String = "v1.0.0"
}
