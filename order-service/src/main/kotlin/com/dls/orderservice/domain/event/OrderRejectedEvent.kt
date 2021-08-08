package com.dls.orderservice.domain.event

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import java.util.*

data class OrderRejectedEvent(
    val orderId: UUID,
    val orderStatus: CreateOrderCommand.OrderStatus = CreateOrderCommand.OrderStatus.REJECTED,
    val reason: String
)