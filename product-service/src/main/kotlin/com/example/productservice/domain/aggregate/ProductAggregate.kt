package com.example.productservice.domain.aggregate

import com.example.productservice.adapter.`in`.command.ReserveProductCommand
import com.example.productservice.domain.event.ProductCreatedEvent
import com.example.productservice.adapter.command.CreateProductCommand
import com.example.productservice.domain.event.ProductReservedEvent
import com.example.productservice.domain.mapper.toProductCreatedEvent
import com.example.productservice.domain.mapper.toProductReservedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.properties.Delegates

@Aggregate
internal class ProductAggregate(){

    @AggregateIdentifier
    private lateinit var productId: UUID
    private lateinit var title:String
    private var price by Delegates.notNull<Double>()
    private var quantity by Delegates.notNull<Int>()


    @CommandHandler
    constructor(createProductCommand: CreateProductCommand) : this() {
        if(createProductCommand.price < 1.0)
            throw  IllegalArgumentException("Price small than 1.0")

        val productCreatedEvent = createProductCommand.toProductCreatedEvent()

        AggregateLifecycle.apply(productCreatedEvent)

       // throw Exception("The event will not be registered. CommandExecutionException will be catch up")
    }

    @CommandHandler
    fun handle(reservedProductCommand: ReserveProductCommand){
        logger.info("ProductAggregate ReserveProductCommand ${reservedProductCommand.orderId}")

        if(quantity < reservedProductCommand.quantity)
            throw  IllegalArgumentException("Insufficient numbers of items in stock")

        val productReservedEvent = reservedProductCommand.toProductReservedEvent()

        AggregateLifecycle.apply(productReservedEvent)


    }

    @EventSourcingHandler
    fun on(productCreatedEvent: ProductCreatedEvent){
        productId = productCreatedEvent.productId
        price = productCreatedEvent.price
        title = productCreatedEvent.title
        quantity = productCreatedEvent.quantity
    }

    @EventSourcingHandler
    fun on(productReservedEvent: ProductReservedEvent){
        quantity -= productReservedEvent.quantity
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ProductAggregate::class.java)
    }

}
