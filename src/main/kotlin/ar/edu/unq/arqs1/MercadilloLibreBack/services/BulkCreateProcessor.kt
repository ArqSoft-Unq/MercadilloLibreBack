package ar.edu.unq.arqs1.MercadilloLibreBack.services

import ar.edu.unq.arqs1.MercadilloLibreBack.listeners.newProductsTopic
import ar.edu.unq.arqs1.MercadilloLibreBack.models.Business
import ar.edu.unq.arqs1.MercadilloLibreBack.models.NewProduct
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.validation.Validation


@Service
class BulkCreateProcessor(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    lateinit var br: BufferedReader
    fun process(seller: Business, file: MultipartFile) {
        br = BufferedReader(InputStreamReader(file.inputStream))
        var line: String?
        while (true) {
            line = br.readLine()
            if (line == null) {
                break
            } else {
                kafkaTemplate.send(newProductsTopic, parseLine(seller, line))
            }
        }
    }

    private fun parseLine(seller: Business, line: String): String {
        return try {
            validate(
                NewProduct(
                    line.split(",")[0],
                    line.split(",")[1],
                    line.split(",")[2].toInt(),
                    line.split(",")[3].toInt()
                )
            ).toJson(seller)
        } catch (e: Exception) {
            "{\"error\": \"${e.message}\"}"
        }
    }

    private fun validate(newProduct: NewProduct): NewProduct {
        val javaxValidator: javax.validation.Validator? = Validation.buildDefaultValidatorFactory().validator
        val errors: Errors = BeanPropertyBindingResult(newProduct, newProduct.javaClass.name)

        SpringValidatorAdapter(javaxValidator!!).validate(newProduct, errors)
        return if (errors.allErrors.isEmpty()) {
            newProduct
        } else {
            throw RuntimeException(ObjectMapper().writeValueAsString(errors.allErrors))
        }
    }
}
