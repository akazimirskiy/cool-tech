package kazimir.cooltech.orders

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface MessageSender {
    @Topic("orders")
    fun send(key: String, value: Order)
}