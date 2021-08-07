package com.dls.orderservice.domain.event

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import java.util.*

data class OrderCreatedEvent(
    val orderId: UUID,
    val userId: UUID,
    val productId: UUID,
    val addressId: String,
    val quantity: Int,
    val orderStatus: CreateOrderCommand.OrderStatus
)