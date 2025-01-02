package kazimir.cooltech.dispatcher

import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import kazimir.cooltech.proto.ClientInfo
import kazimir.cooltech.proto.SubscriptionServiceGrpc
import org.slf4j.LoggerFactory

@Singleton
class SubscriptionService (private val notificationService: NotificationService): SubscriptionServiceGrpc.SubscriptionServiceImplBase() {

    private val log = LoggerFactory.getLogger(DispatcherService::class.java)

    override fun subscribe(
        request: ClientInfo.SubscribeRequest,
        responseObserver: StreamObserver<ClientInfo.SubscribeResponse>
    ) {
        log.info("Got subscription request: $request")
        notificationService.subscribe(request.clientAddress, request.clientPort, request.clientName)
    }

    override fun unSubscribe(
        request: ClientInfo.UnSubscribeRequest,
        responseObserver: StreamObserver<ClientInfo.SubscribeResponse>
    ) {
        log.info("Got unsubscription request: $request")
        notificationService.unSubscribe(request.clientName)
    }
}