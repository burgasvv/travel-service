package org.burgas.travelservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.burgas.travelservice.dto.identity.IdentityFullResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer

@Configuration
class KafkaConsumerConfig {

    @Bean
    fun consumerConfig(): Map<String, Any> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to "consumer-config-group-id",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JacksonJsonDeserializer::class.java,
            JacksonJsonDeserializer.TYPE_MAPPINGS to
                    "org.burgas.travelservice.dto.identity.IdentityFullResponse:org.burgas.travelservice.dto.identity.IdentityFullResponse"
        )
    }

    @Bean
    fun identityKafkaConsumer(): KafkaConsumer<String, IdentityFullResponse> {
        val consumer = KafkaConsumer<String, IdentityFullResponse>(this.consumerConfig())
        consumer.subscribe(listOf("identity-kafka-topic"))
        return consumer
    }
}