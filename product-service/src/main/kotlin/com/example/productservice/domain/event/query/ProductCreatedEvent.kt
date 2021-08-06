package com.example.productservice.domain.event.query

import java.util.*

data class ProductCreatedEvent(
    val productId: UUID,
    val title:String,
    val price:Double,
    val quantity: Int)