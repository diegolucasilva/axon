package com.dls.orderservice.adapter.`in`.saga

import com.dls.orderservice.adapter.`in`.command.ApproveOrderCommand
import com.dls.orderservice.adapter.`in`.command.RejectOrderCommand
import com.dls.orderservice.domain.event.OrderApprovedEvent
import com.dls.orderservice.domain.event.OrderCreatedEvent
import com.dls.orderservice.domain.event.OrderRejectedEvent
import com.dls.orderservice.domain.query.FindOrderQuery
import com.dls.orderservice.domain.query.OrderSummary
import com.dls.paymentservice.adapter.`in`.command.ProcessPaymentCommand
import com.dls.paymentservice.domain.event.PaymentProcessedEvent
import com.dls.productservice.adapter.`in`.command.CancelProductReservationCommand
import com.dls.userservice.adapter.`in`.controller.FetchUserPaymentDetailsQuery
import com.dls.userservice.domain.User
import com.dls.productservice.adapter.`in`.command.ReserveProductCommand
import com.dls.productservice.domain.event.ProductReservationCancelledEvent
import com.dls.productservice.domain.event.ProductReservedEvent
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.CommandResultMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

@Saga
class OrderSaga() {

    @Autowired
    @Transient
    private var commandGateway: CommandGateway? = null

    @Autowired
    @Transient
    private val queryGateway: QueryGateway? = null

    @Autowired
    @Transient
    private val queryUpdateEmitter: QueryUpdateEmitter? = null

    @Autowired
    @Transient
    private val deadlineManager: DeadlineManager? = null

    private lateinit var deadLineId: String

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
        commandGateway?.send<ReserveProductCommand, Any>(reserveProductCommand,
            { commandMessage: CommandMessage<out ReserveProductCommand>, commandResultMessage: CommandResultMessage<*> ->
                if(commandResultMessage.isExceptional){
                    logger.error("Saga OrderCreatedEvent failed for orderId ${orderCreatedEvent.orderId}")
                    val rejectOrderCommand = RejectOrderCommand(
                        orderId = orderCreatedEvent.orderId,
                        reason = commandResultMessage.exceptionResult().message!!
                    )
                    commandGateway?.send<Any>(rejectOrderCommand)
                }

                logger.info("Result ${commandResultMessage}")
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
            cancelProductReservation(productReservedEvent, "Could not fetch for user details ${userDetails?.userId}\"")
        }
        logger.info("Successfully fetched details for user ${userDetails?.userId}")

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
            deadLineId = deadlineManager?.schedule(
                Duration.of(10, ChronoUnit.SECONDS),
                PAYMENT_PROCESSING_TIMEOUT_DEADLINE,productReservedEvent)!!
            if(true) return  //bug to test deadline

            //val result  = commandGateway?.sendAndWait<Any>(processPaymentCommand, 10, TimeUnit.SECONDS)
            val result  = commandGateway?.sendAndWait<Any>(processPaymentCommand) //USING DEADLINE MANAGER

            if(result == null){
                logger.error("The ProcessPaymentCommand is NULL.Starting compensation transaction")
                cancelProductReservation(productReservedEvent, "Could not process payment ${userDetails?.userId}\"")
                return
            }
        }catch (ex: Exception){
            logger.error("${ex.message} ProcessPaymentCommand failed. Starting compensation transaction")
            cancelProductReservation(productReservedEvent, ex.message!!)
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    fun handle(paymentProcessedEvent: PaymentProcessedEvent){
        cancelDeadLine()
        logger.info("Saga PaymentProcessedEvent for user ${paymentProcessedEvent.orderId}")

        val approveOrderCommand = ApproveOrderCommand(paymentProcessedEvent.orderId)
        commandGateway?.send<ReserveProductCommand>(approveOrderCommand)

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    fun handle(orderApprovedEvent: OrderApprovedEvent){
        logger.info("Saga orderApprovedEvent for user ${orderApprovedEvent.orderId}")
        queryUpdateEmitter?.emit(FindOrderQuery::class.java,
            {true},
            OrderSummary(orderApprovedEvent.orderId,orderApprovedEvent.orderStatus)
             )
    }

    @SagaEventHandler(associationProperty = "orderId")
    fun handle(productReservationCancelledEvent: ProductReservationCancelledEvent){
        logger.error("Saga productReservationCancelledEvent for user ${productReservationCancelledEvent.orderId}")
        val rejectOrderCommand = RejectOrderCommand(
            orderId = productReservationCancelledEvent.orderId,
            reason = productReservationCancelledEvent.reason
        )
        commandGateway?.send<Any>(rejectOrderCommand)
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    fun handle(orderRejectedEvent: OrderRejectedEvent){
        logger.error("Saga OrderRejectedEvent. Order successfully rejected for user ${orderRejectedEvent.orderId}")
        queryUpdateEmitter?.emit(FindOrderQuery::class.java,
            {true},
            OrderSummary(orderRejectedEvent.orderId,orderRejectedEvent.orderStatus,orderRejectedEvent.reason)
        )

    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_TIMEOUT_DEADLINE)
    fun handlePaymentDeadLine(productReservedEvent: ProductReservedEvent){
        logger.error("Saga DeadlineHandler. Payment processing deadline took place. " +
                "Sending compensation command to cancel t for user ${productReservedEvent.orderId}")
        cancelProductReservation(productReservedEvent,"DeadlineHandler. Payment processing deadline took place")
    }






    private fun cancelProductReservation(productReservedEvent: ProductReservedEvent,reason: String){
        cancelDeadLine()
        val cancelProductReservationCommand = CancelProductReservationCommand(
            orderId = productReservedEvent.orderId,
            productId = productReservedEvent.productId,
            userId = productReservedEvent.userId,
            quantity = 1,
            reason = reason
        )
        commandGateway?.send<Any>(cancelProductReservationCommand)
    }

    private fun cancelDeadLine() =
        deadlineManager?.cancelSchedule(PAYMENT_PROCESSING_TIMEOUT_DEADLINE,deadLineId)

    companion object {
        private const val PAYMENT_PROCESSING_TIMEOUT_DEADLINE= "payment-processing-deadline"
        private val logger = LoggerFactory.getLogger(OrderSaga::class.java)
    }

}
