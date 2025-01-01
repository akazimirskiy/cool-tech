package kazimir.cooltech.orders

import jakarta.inject.Singleton
import java.util.*
import kotlin.random.Random

@Singleton
class OrderGenerator {
    private val users : Array<UUID> = Array(5) {UUID.randomUUID()}
    private val products : Array<UUID> = Array(15) {UUID.randomUUID()}
    fun generateOrder(): Order {
        return Order(UUID.randomUUID(), users[Random.nextInt(users.size)],
            products[Random.nextInt(products.size)], Random.nextInt(1, 11), Random.nextDouble(1.0, 11.0)
        )
    }
}