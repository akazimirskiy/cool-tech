package kazimir.cooltech.orders

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface MessageSender {
    @Topic("orders")
    fun send(@KafkaKey key: String, value: Order)
}