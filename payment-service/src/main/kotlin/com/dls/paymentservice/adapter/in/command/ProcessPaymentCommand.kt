package com.dls.paymentservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class ProcessPaymentCommand(

    @TargetAggregateIdentifier
    val paymentId: UUID,
    val orderId: UUID,
    val paymentDetails: PaymentDetails
){
    data class PaymentDetails(
        val name: String,
        val cardNumber: String,
        val validUntilMonth: Int,
        val validUntilYear: Int,
        val cvv: String
    )
}
