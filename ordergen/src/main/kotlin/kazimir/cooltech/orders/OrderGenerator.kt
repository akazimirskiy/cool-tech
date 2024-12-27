package kazimir.cooltech.orders

import jakarta.inject.Singleton
import java.util.UUID
import kotlin.random.Random

@Singleton
class OrderGenerator {
    fun generateOrder(): Order {
        return Order(UUID.randomUUID(), UUID.randomUUID(), Random(100).nextInt())
    }
}