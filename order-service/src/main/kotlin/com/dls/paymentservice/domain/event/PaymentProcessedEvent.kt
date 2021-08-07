package com.dls.paymentservice.domain.event

import java.util.*

class PaymentProcessedEvent(
    val paymentId: UUID,
    val orderId: UUID
)