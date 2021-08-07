package com.dls.productservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class ReserveProductCommand(
    @TargetAggregateIdentifier
    val productId: UUID,
    val orderId: UUID,
    val userId: UUID,
    val quantity: Int)
