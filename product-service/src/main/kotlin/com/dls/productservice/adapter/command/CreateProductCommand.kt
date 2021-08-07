package com.dls.productservice.adapter.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateProductCommand(
    @TargetAggregateIdentifier
    val productId: UUID,
    val title:String,
    val price:Double,
    val quantity: Int)
