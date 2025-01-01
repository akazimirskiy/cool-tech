package kazimir.cooltech.orders

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.util.*

@Introspected
@Serdeable
data class Order(var id: UUID, var userId:UUID, var productId: UUID, var quantity: Int, var price: Double)