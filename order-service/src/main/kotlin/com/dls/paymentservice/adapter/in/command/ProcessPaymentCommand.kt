package com.dls.paymentservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class ProcessPaymentCommand(

    @TargetAggregateIdentifier
    var paymentId: UUID,
    var orderId: UUID,
    var paymentDetails: PaymentDetails
){
    data class PaymentDetails(
        var name: String,
        var cardNumber: String,
        var validUntilMonth: Int,
        var validUntilYear: Int,
        var cvv: String
    )
}