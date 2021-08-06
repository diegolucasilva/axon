package com.dls.orderservice.adapter.`in`.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*


data class CreateOrderCommand(
    @TargetAggregateIdentifier
    val orderId: UUID,
    val userId: UUID,
    val productId: String,
    val addressId: String,
    val quantity: Int,
    val orderStatus: OrderStatus)


enum class OrderStatus{CREATED, APPROVED, REJECTED}


