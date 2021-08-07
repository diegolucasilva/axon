package com.example.productservice.adapter.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.springframework.stereotype.Component
import java.util.*

data class CreateProductCommand(
    @TargetAggregateIdentifier
    var productId: UUID,
    var title:String,
    var price:Double,
    var quantity: Int)
