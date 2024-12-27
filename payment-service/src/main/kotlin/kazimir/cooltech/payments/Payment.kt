package kazimir.cooltech.payments

import java.math.BigInteger
import java.util.UUID

class Payment(var id: UUID, var orderId: UUID, var amount: BigInteger)