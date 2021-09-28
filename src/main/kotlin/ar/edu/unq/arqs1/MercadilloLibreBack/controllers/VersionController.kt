package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public")
class VersionController {
    @GetMapping("/version")
    fun index(): String = "v1.0.0"
}