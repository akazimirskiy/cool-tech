package kazimir.cooltech.orders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@KafkaListener
class OrderListener(private val orderDispatcherClient: OrderDispatcherClient) {
    private val log: Logger = LoggerFactory.getLogger(OrderListener::class.java)
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder()
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build())
        .disable(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    @Topic("orders")
    fun receive(key: String, orderStr: String) {
        log.info("Received order: $orderStr")
        val order = objectMapper.readValue(orderStr, Order::class.java)
        log.info("Parsed order: $order")
        orderDispatcherClient.dispatchOrder(order)
    }
}