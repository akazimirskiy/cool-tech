package kazimir.cooltech.orders

import io.micronaut.core.annotation.Introspected
import java.util.UUID

@Introspected
class Order(var id: UUID, var productId: UUID, var quantity: Int)