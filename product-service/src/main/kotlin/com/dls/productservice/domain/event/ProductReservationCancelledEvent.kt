package com.dls.productservice.domain.event

import java.util.*

data class ProductReservationCancelledEvent(
    val productId: UUID,
    val orderId: UUID,
    val userId: UUID,
    val quantity: Int,
    val reason: String)