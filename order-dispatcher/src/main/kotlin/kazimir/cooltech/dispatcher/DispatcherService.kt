package kazimir.cooltech.dispatcher

import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import kazimir.cooltech.proto.DispatchResponseOuterClass.DispatchResponse
import kazimir.cooltech.proto.DispatcherServiceGrpc
import kazimir.cooltech.proto.OrderOuterClass
import org.slf4j.LoggerFactory
import java.util.*

@Singleton
class DispatcherService(private val notificationService: NotificationService) : DispatcherServiceGrpc.DispatcherServiceImplBase() {
    private val log = LoggerFactory.getLogger(DispatcherService::class.java)

    override fun dispatchOrder(
        request: OrderOuterClass.Order,
        responseObserver: StreamObserver<DispatchResponse>) {
        log.info("Got order to dispatch: ${request.id}")
        notificationService.notifyAll(request)
        responseObserver.onNext(
            DispatchResponse.newBuilder()
                .setOrderId(request.id).setId(UUID.randomUUID().toString()).setStatus("SUCCESS")
                .build())
        responseObserver.onCompleted()
    }
}