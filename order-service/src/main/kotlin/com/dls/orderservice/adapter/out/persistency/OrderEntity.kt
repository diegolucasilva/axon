package com.dls.orderservice.adapter.out.persistency

import com.dls.orderservice.adapter.`in`.command.OrderStatus
import java.util.*
import javax.persistence.*

@Entity
data class OrderEntity (
    @Id
    @Column(unique = true)
    val orderId: UUID,
    val userId: UUID,
    val productId: String,
    val addressId: String,
    val quantity: Int,
    @Enumerated(EnumType.STRING)
    val orderStatus: OrderStatus
)