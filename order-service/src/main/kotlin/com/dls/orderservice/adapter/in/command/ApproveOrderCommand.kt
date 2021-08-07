package com.dls.orderservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class ApproveOrderCommand(
    @TargetAggregateIdentifier
    val orderId: UUID
)