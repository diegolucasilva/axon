package com.dls.paymentservice.domain.event

import java.util.*

class PaymentProcessedEvent(
    var paymentId: UUID,
    var orderId: UUID)