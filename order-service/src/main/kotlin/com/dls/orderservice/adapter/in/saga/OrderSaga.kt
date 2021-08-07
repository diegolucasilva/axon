package com.dls.orderservice.adapter.`in`.saga

import com.dls.orderservice.domain.event.OrderCreatedEvent
import com.example.productservice.adapter.`in`.command.ReserveProductCommand
import com.example.productservice.domain.event.ProductReservedEvent
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.CommandResultMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Saga
class OrderSaga() {

    @Autowired
    @Transient
    private var commandGateway: CommandGateway? = null

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    fun handle(orderCreatedEvent: OrderCreatedEvent) {
        logger.info("Saga OrderCreatedEvent for orderId ${orderCreatedEvent.orderId}")

       val reserveProductCommand = ReserveProductCommand(
            orderId = orderCreatedEvent.orderId,
            productId = orderCreatedEvent.productId,
            userId = orderCreatedEvent.userId,
            quantity = 1,
        )

       /* val reserveProductCommand = ReserveProductCommand.
        builder().
        productId(orderCreatedEvent.productId).
        orderId(orderCreatedEvent.orderId).
        userId(orderCreatedEvent.userId).
        quantity(1).
        build()*/


        commandGateway?.send<ReserveProductCommand, Any>(reserveProductCommand,
            { commandMessage: CommandMessage<out ReserveProductCommand>, commandResultMessage: CommandResultMessage<*> ->
                if (commandResultMessage.isExceptional) {
                    logger.error("Result ${commandResultMessage}")
                }
                logger.error("Result ${commandResultMessage}")
            })
    }

    @SagaEventHandler(associationProperty = "orderId")
    fun handle(productReservedEvent: ProductReservedEvent) {
        logger.info("Saga ProductReservedEvent for orderId ${productReservedEvent.orderId}")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderSaga::class.java)
    }

}
