package ar.edu.unq.arqs1.MercadilloLibreBack.controllers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class VersionControllerTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `The versions endpoint works`() {
        val entity = restTemplate.getForEntity("/public/version", String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
}