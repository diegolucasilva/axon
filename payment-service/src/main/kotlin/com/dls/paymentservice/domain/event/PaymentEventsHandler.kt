package com.dls.paymentservice.domain.event

import com.dls.paymentservice.domain.port.out.persistency.PaymentRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import toPaymentEntity


@Component
class PaymentEventsHandler(private val paymentRepository: PaymentRepository) {

    @EventHandler
    fun on(paymentProcessedEvent: PaymentProcessedEvent) {
        logger.info("EventHandler PaymentProcessedEvent is called for orderId: " + paymentProcessedEvent.orderId)
        val paymentEntity = paymentProcessedEvent.toPaymentEntity()
        paymentRepository.save(paymentEntity)
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(PaymentEventsHandler::class.java)
    }
}