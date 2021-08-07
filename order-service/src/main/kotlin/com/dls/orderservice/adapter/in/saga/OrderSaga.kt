package com.dls.orderservice.adapter.`in`.saga

import com.dls.orderservice.adapter.`in`.command.ApproveOrderCommand
import com.dls.orderservice.domain.event.OrderApprovedEvent
import com.dls.orderservice.domain.event.OrderCreatedEvent
import com.dls.paymentservice.adapter.`in`.command.ProcessPaymentCommand
import com.dls.paymentservice.domain.event.PaymentProcessedEvent
import com.dls.userservice.adapter.`in`.controller.FetchUserPaymentDetailsQuery
import com.dls.userservice.domain.User
import com.dls.productservice.adapter.`in`.command.ReserveProductCommand
import com.dls.productservice.domain.event.ProductReservedEvent
import com.dls.userservice.domain.PaymentDetails
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.CommandResultMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.TimeUnit

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
        val userDetails = queryGateway?.query(
            FetchUserPaymentDetailsQuery(productReservedEvent.userId),
            ResponseTypes.instanceOf(User::class.java)
        )?.join()

        if(userDetails == null){
            logger.info("Starting compensation transaction")
        }

        val processPaymentCommand = ProcessPaymentCommand(
            paymentId = UUID.randomUUID(),
            orderId = productReservedEvent.orderId,
            paymentDetails= ProcessPaymentCommand.PaymentDetails(
                cardNumber =userDetails?.paymentDetails!!.cardNumber,
                cvv= userDetails.paymentDetails.cvv,
                name= userDetails.paymentDetails.name,
                validUntilMonth=userDetails.paymentDetails.validUntilMonth,
                validUntilYear=userDetails.paymentDetails.validUntilYear)
        )

        try {
            val result  = commandGateway?.sendAndWait<Any>(processPaymentCommand, 10, TimeUnit.SECONDS)
            if(result == null){
                logger.error("The ProcessPaymentCommand is NULL.Starting compensation transaction")
            }
        }catch (ex: Exception){
            logger.error("${ex.message} ProcessPaymentCommand failed. Starting compensation transaction")
        }
        logger.info("Successfully fetched user payment details for user ${userDetails?.userId}")
    }

    @SagaEventHandler(associationProperty = "orderId")
    fun handle(paymentProcessedEvent: PaymentProcessedEvent){
        logger.info("Saga PaymentProcessedEvent for user ${paymentProcessedEvent.orderId}")

        val approveOrderCommand = ApproveOrderCommand(paymentProcessedEvent.orderId)
        commandGateway?.send<ReserveProductCommand>(approveOrderCommand)

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    fun handle(orderApprovedEvent: OrderApprovedEvent){
        logger.info("Saga Order approved for user ${orderApprovedEvent.orderId}")
       // SagaLifecycle.end()

    }




    companion object {
        private val logger = LoggerFactory.getLogger(OrderSaga::class.java)
    }

}
