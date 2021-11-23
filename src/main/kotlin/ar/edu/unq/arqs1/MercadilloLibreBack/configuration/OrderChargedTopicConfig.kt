package ar.edu.unq.arqs1.MercadilloLibreBack.configuration

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.config.TopicConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder

const val orderChargedTopic: String = "order-charged"

@EnableKafka
@Configuration
class KafkaOrderChargedProducerConfig {
    @Bean
    fun createOrderChargedTopic(): NewTopic {
        return TopicBuilder.name(orderChargedTopic)
            .partitions(1)
            .replicas(1)
            .config(TopicConfig.RETENTION_MS_CONFIG, "-1")
            .config(TopicConfig.RETENTION_BYTES_CONFIG, "-1")
            .config(TopicConfig.CLEANUP_POLICY_CONFIG, "delete")
            .build()
    }
}
