package com.dls.orderservice.domain.event

import com.dls.orderservice.adapter.`in`.command.OrderStatus
import java.util.*

data class OrderCreatedEvent(
    val orderId: UUID,
    val userId: UUID,
    val productId: String,
    val addressId: String,
    val quantity: Int,
    val orderStatus: OrderStatus
)