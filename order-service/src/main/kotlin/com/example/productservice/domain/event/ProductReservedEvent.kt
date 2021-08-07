package com.example.productservice.domain.event

import java.util.*

data class ProductReservedEvent(
    val productId: UUID,
    val orderId: UUID,
    val userId: UUID,
    val quantity: Int)