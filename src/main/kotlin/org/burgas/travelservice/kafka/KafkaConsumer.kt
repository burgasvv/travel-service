package org.burgas.travelservice.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.burgas.travelservice.dto.identity.IdentityFullResponse
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer {

    @KafkaListener(groupId = "consumer-config-group-id", topics = ["identity-kafka-topic"])
    fun kafkaListenerIdentityFullResponse(consumerRecord: ConsumerRecord<String, IdentityFullResponse>) {
        println(consumerRecord.value())
    }
}