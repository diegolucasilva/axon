package com.dls.productservice.adapter.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateProductCommand(
    @TargetAggregateIdentifier
    var productId: UUID,
    var title:String,
    var price:Double,
    var quantity: Int)
