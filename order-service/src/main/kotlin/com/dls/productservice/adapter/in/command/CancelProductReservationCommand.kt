package com.dls.productservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CancelProductReservationCommand(
    @TargetAggregateIdentifier
    val productId: UUID,
    val orderId: UUID,
    val userId: UUID,
    val quantity: Int,
    val reason: String)
