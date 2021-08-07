package com.dls.paymentservice.domain.agregate

import com.dls.paymentservice.adapter.`in`.command.ProcessPaymentCommand
import com.dls.paymentservice.domain.event.PaymentProcessedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.*


@Aggregate
class PaymentAggregate() {

    @AggregateIdentifier
    private lateinit var paymentId: UUID
    private lateinit var orderId: UUID

    @CommandHandler
    constructor(processPaymentCommand: ProcessPaymentCommand) : this() {
        logger.info("CommandHandler ProcessPaymentCommand for orderId: " + processPaymentCommand.orderId)

        requireNotNull(processPaymentCommand.paymentDetails) { "Missing payment details" }
        requireNotNull(processPaymentCommand.orderId) { "Missing orderId" }
        requireNotNull(processPaymentCommand.paymentId) { "Missing paymentId" }
        AggregateLifecycle.apply(
            PaymentProcessedEvent(
                processPaymentCommand.paymentId,
                processPaymentCommand.orderId,
            )
        )
    }

    @EventSourcingHandler
    fun on(paymentProcessedEvent: PaymentProcessedEvent) {
        logger.info("CommandHandler PaymentProcessedEvent for orderId: " + paymentProcessedEvent.orderId)

        paymentId = paymentProcessedEvent.paymentId
        orderId = paymentProcessedEvent.orderId
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(PaymentAggregate::class.java)
    }


}