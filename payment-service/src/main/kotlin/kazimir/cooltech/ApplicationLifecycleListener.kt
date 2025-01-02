package kazimir.cooltech

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Value
import io.micronaut.context.event.ApplicationEvent
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.ShutdownEvent
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton
import kazimir.cooltech.proto.SubscriptionServiceGrpc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Singleton
class ApplicationLifecycleListener : ApplicationEventListener<ApplicationEvent> {
    @Value("\${dispatcher.host}")
    lateinit var dispatcherHost: String
    @Value("\${dispatcher.port}")
    var dispatcherPort: Int = 0
    @Value("\${micronaut.application.name}")
    lateinit var applicationHost: String
    lateinit var applicationName: String

    private val log: Logger = LoggerFactory.getLogger(ApplicationLifecycleListener::class.java)
    private lateinit var managedChannel : ManagedChannel
    private lateinit var dispatcherSubscriptionService: SubscriptionServiceGrpc.SubscriptionServiceFutureStub

    @PostConstruct
    fun init() {
        applicationName = applicationHost + ": " + UUID.randomUUID().toString()
        managedChannel = ManagedChannelBuilder
            .forAddress(dispatcherHost, dispatcherPort)
            .usePlaintext()
            .idleTimeout(5, TimeUnit.SECONDS)
            .build()
        dispatcherSubscriptionService = SubscriptionServiceGrpc.newFutureStub(managedChannel)
        log.info("Created channel to dispatcher service: $dispatcherHost:$dispatcherPort")
    }

    override fun onApplicationEvent(event: ApplicationEvent?) {
        when (event) {
            is StartupEvent -> {
                log.info("Subscribing to dispatcher service")
                dispatcherSubscriptionService.subscribe(
                    kazimir.cooltech.proto.ClientInfo.SubscribeRequest.newBuilder()
                        .setClientAddress(applicationHost)
                        .setClientPort(50050)
                        .setClientName(applicationName)
                        .build()
                )
                log.info("Subscribed to dispatcher service")
            }
            is ShutdownEvent -> {
                println("Unsubscribing from dispatcher service on shutdown")
                dispatcherSubscriptionService.unSubscribe(
                    kazimir.cooltech.proto.ClientInfo.UnSubscribeRequest.newBuilder()
                        .setClientName(applicationName)
                        .build()
                )
            }
        }
    }
}