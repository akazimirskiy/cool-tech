package kazimir.cooltech.orders

import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
@Singleton
class OrderSender(private val messageSender: MessageSender) {

    private val log: Logger = LoggerFactory.getLogger(OrderSender::class.java)

    fun sendOrder(value: Order) {
        messageSender.send(value.id.toString(), value)
        log.info("Message sent: ${value.id}")
    }
}