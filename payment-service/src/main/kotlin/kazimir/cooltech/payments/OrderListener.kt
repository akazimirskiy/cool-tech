package kazimir.cooltech.payments

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@KafkaListener
class OrderListener {
    private val log: Logger = LoggerFactory.getLogger(OrderListener::class.java)

    @Topic("orders")
    fun receive(key: String, orderStr: String) {
        log.info("Received order: $orderStr")
    }
}