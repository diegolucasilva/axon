package com.dls.orderservice.adapter.`in`.saga

import com.dls.orderservice.domain.event.OrderCreatedEvent
import com.dls.userservice.adapter.`in`.controller.FetchUserPaymentDetailsQuery
import com.dls.userservice.domain.User
import com.dls.productservice.adapter.`in`.command.ReserveProductCommand
import com.dls.productservice.domain.event.ProductReservedEvent
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.CommandResultMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Saga
class OrderSaga() {

    @Autowired
    @Transient
    private var commandGateway: CommandGateway? = null
    @Autowired
    @Transient
    private val queryGateway: QueryGateway? = null

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    fun handle(orderCreatedEvent: OrderCreatedEvent) {
        logger.info("Saga OrderCreatedEvent for orderId ${orderCreatedEvent.orderId}")

       val reserveProductCommand = com.dls.productservice.adapter.`in`.command.ReserveProductCommand(
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


        commandGateway?.send<com.dls.productservice.adapter.`in`.command.ReserveProductCommand, Any>(reserveProductCommand,
            { commandMessage: CommandMessage<out com.dls.productservice.adapter.`in`.command.ReserveProductCommand>, commandResultMessage: CommandResultMessage<*> ->
                if (commandResultMessage.isExceptional) {
                    logger.error("Result ${commandResultMessage}")
                }
                logger.error("Result ${commandResultMessage}")
            })
    }

    @SagaEventHandler(associationProperty = "orderId")
    fun handle(productReservedEvent: com.dls.productservice.domain.event.ProductReservedEvent) {
        logger.info("Saga ProductReservedEvent for orderId ${productReservedEvent.orderId}")
        val userDetails = queryGateway?.query(
            FetchUserPaymentDetailsQuery(productReservedEvent.userId),
            ResponseTypes.instanceOf(User::class.java)
        )?.join()

        if(userDetails == null){
            logger.info("Starting compensation transaction")
        }
        logger.info("Successfully fetched user payment details for user ${userDetails?.userId}")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderSaga::class.java)
    }

}
