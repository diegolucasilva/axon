package com.dls.orderservice.adapter.out.persistency

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import java.util.*
import javax.persistence.*

@Entity
data class OrderEntity (
    @Id
    @Column(unique = true)
    val orderId: UUID,
    val userId: UUID,
    val productId: UUID,
    val addressId: String,
    val quantity: Int,
    @Enumerated(EnumType.STRING)
    var orderStatus: CreateOrderCommand.OrderStatus
)