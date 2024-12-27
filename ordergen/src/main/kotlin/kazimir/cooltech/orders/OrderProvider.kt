package kazimir.cooltech.orders

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton

@Singleton
class OrderProvider(private val orderGenerator: OrderGenerator,
                    private val orderSender: OrderSender) {

    @Scheduled(fixedRate = "1s")
    fun provideOrder() {
        orderSender.sendOrder(orderGenerator.generateOrder())
    }


}