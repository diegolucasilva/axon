package com.dls.orderservice.domain.event

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import java.util.*

data class OrderApprovedEvent(
    val orderId: UUID,
    val orderStatus: CreateOrderCommand.OrderStatus = CreateOrderCommand.OrderStatus.APPROVED
)