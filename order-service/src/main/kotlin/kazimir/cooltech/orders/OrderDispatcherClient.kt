package kazimir.cooltech.orders

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import kazimir.cooltech.proto.DispatcherServiceGrpc
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Singleton
class OrderDispatcherClient {
    @Value("\${dispatcher.server.host}")
    private lateinit var dispatcherAddress: String
    @Value("\${dispatcher.server.port}")
    private var dispatcherPort: Int = 0
    private val orderProcessorId = UUID.randomUUID().toString()

    private val log = LoggerFactory.getLogger(OrderListener::class.java)
    private lateinit var managedChannel : ManagedChannel
    private lateinit var dispatcherService: DispatcherServiceGrpc.DispatcherServiceFutureStub
    private val executor = Executors.newFixedThreadPool(3)

    @PostConstruct
    fun init() {
        managedChannel = ManagedChannelBuilder
            .forAddress(dispatcherAddress, dispatcherPort)
            .usePlaintext()
            .idleTimeout(5, TimeUnit.SECONDS)
            .build()
        dispatcherService = DispatcherServiceGrpc.newFutureStub(managedChannel)
        log.info("Created channel to dispatcher service: $dispatcherAddress:$dispatcherPort")
    }

    fun dispatchOrder(order: Order) {
        log.info("Dispatching order: ${order.id}")
        executor.submit {
            log.info("Submitting order: ${order.id}")
            val orderToDispatch = kazimir.cooltech.proto.OrderOuterClass.Order.newBuilder()
                .setId(order.id.toString())
                .setProductId(order.productId.toString())
                .setUserId(order.userId.toString())
                .setQuantity(order.quantity)
                .setPrice(order.price)
                .setOrderProcessorId(orderProcessorId)
                .build()
            val response = dispatcherService.withDeadlineAfter(5, TimeUnit.SECONDS).dispatchOrder(orderToDispatch)
            if (!response.isDone && !response.isCancelled) {
                response.addListener({
                    log.info("Order: ${orderToDispatch.id}  dispatched with the result: ${response.get().status}")
                }, executor)
            } else {
                log.info("Dispatching result: ${response.state().name}")
            }
        }
    }
}

//fun main(args: Array<String>) {
//    val client = OrderDispatcherClient().apply {
//        dispatcherAddress = "localhost" // Установите реальные значения или подтяните из конфигурации
//        dispatcherPort = 50051
//        init() // Вызываем метод инициализации
//    }
//
//    val order = Order(
//        id = java.util.UUID.randomUUID(),
//        userId = java.util.UUID.randomUUID(),
//        productId = java.util.UUID.randomUUID(),
//        quantity = 1,
//        price = 100.0
//    )
//    client.dispatchOrder(order)
//}