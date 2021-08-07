package com.dls.paymentservice.adapter.out.persistency

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class PaymentEntity(
    @Id
    val paymentId: UUID,
    val orderId:UUID
)