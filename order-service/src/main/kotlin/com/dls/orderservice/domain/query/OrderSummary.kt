package com.dls.orderservice.domain.query

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import java.util.*

data class OrderSummary(
    val orderId: UUID,
    val orderStatus: CreateOrderCommand.OrderStatus,
    val reason: String?= ""
    )