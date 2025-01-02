package kazimir.cooltech.dispatcher

import io.grpc.ManagedChannelBuilder
import jakarta.inject.Singleton
import kazimir.cooltech.proto.NotifiableSubscriberGrpc
import kazimir.cooltech.proto.OrderOuterClass
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit

@Singleton
class NotificationService {
    private val log = LoggerFactory.getLogger(NotificationService::class.java)
    private val clients: ConcurrentMap<String, NotifiableSubscriberGrpc.NotifiableSubscriberFutureStub> = ConcurrentHashMap()

    fun subscribe(clientAddress: String, clientPort: Int, clientName: String) {
        val clientChannel = ManagedChannelBuilder
            .forAddress(clientAddress, clientPort)
            .usePlaintext()
            .idleTimeout(5, TimeUnit.SECONDS)
            .build()
        clients[clientName] = NotifiableSubscriberGrpc.newFutureStub(clientChannel)
        log.info("Subscribed $clientName")
    }

    fun unSubscribe(clientName: String) {
        clients.remove(clientName)
        log.info("Unsubscribed $clientName")
    }

    fun notifyAll(order: OrderOuterClass.Order) {
        clients.forEach { (clientName, client) ->
            client.acceptNotification(order)
            log.info("Notified $clientName about order ${order.id}")
        }
    }
}