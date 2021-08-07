package com.example.productservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class ReserveProductCommand(
    @TargetAggregateIdentifier
    var productId: UUID,
    var orderId: UUID,
    var userId: UUID,
    var quantity: Int)
