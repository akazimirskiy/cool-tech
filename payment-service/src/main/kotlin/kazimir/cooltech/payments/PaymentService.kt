package kazimir.cooltech.payments

import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import kazimir.cooltech.proto.DispatchResponseOuterClass
import kazimir.cooltech.proto.NotifiableSubscriberGrpc
import kazimir.cooltech.proto.OrderOuterClass
import org.slf4j.LoggerFactory

@Singleton
class PaymentService : NotifiableSubscriberGrpc.NotifiableSubscriberImplBase() {
    private val log = LoggerFactory.getLogger(PaymentService::class.java)

    override fun acceptNotification(
        request: OrderOuterClass.Order?,
        responseObserver: StreamObserver<DispatchResponseOuterClass.DispatchResponse>?) {
        log.info("Got notification for order: ${request?.id}")
        responseObserver?.onNext(
            DispatchResponseOuterClass.DispatchResponse.newBuilder()
                .setOrderId(request?.id)
                .setId(java.util.UUID.randomUUID().toString())
                .setStatus("SUCCESS")
                .build())
        responseObserver?.onCompleted()
    }
}
