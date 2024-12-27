package kazimir.cooltech.orders

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.util.UUID

@Introspected
@Serdeable
class Order(var id: UUID, var productId: UUID, var quantity: Int)