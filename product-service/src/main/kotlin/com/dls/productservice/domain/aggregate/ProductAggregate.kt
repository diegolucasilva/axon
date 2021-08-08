package com.dls.productservice.domain.aggregate

import com.dls.productservice.adapter.`in`.command.CancelProductReservationCommand
import com.dls.productservice.adapter.`in`.command.ReserveProductCommand
import com.dls.productservice.adapter.command.CreateProductCommand
import com.dls.productservice.domain.event.ProductCreatedEvent
import com.dls.productservice.domain.event.ProductReservationCancelledEvent
import com.dls.productservice.domain.event.ProductReservedEvent
import com.dls.productservice.domain.mapper.toProductCreatedEvent
import com.dls.productservice.domain.mapper.toProductReservationCancelledEvent
import com.dls.productservice.domain.mapper.toProductReservedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.properties.Delegates

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
internal class ProductAggregate(){

    @AggregateIdentifier
    private lateinit var productId: UUID
    private lateinit var title:String
    private var price by Delegates.notNull<Double>()
    private var quantity by Delegates.notNull<Int>()


    @CommandHandler
    constructor(createProductCommand: CreateProductCommand) : this() {
        logger.info("CommandHandler CreateProductCommand ${createProductCommand.productId}")

        if(createProductCommand.price < 1.0)
            throw  IllegalArgumentException("Price small than 1.0")

        val productCreatedEvent = createProductCommand.toProductCreatedEvent()

        AggregateLifecycle.apply(productCreatedEvent)

       // throw Exception("The event will not be registered. CommandExecutionException will be catch up")
    }

    @CommandHandler
    fun handle(reservedProductCommand: ReserveProductCommand){
        logger.info("CommandHandler ReserveProductCommand ${reservedProductCommand.orderId}")

        if(quantity < reservedProductCommand.quantity)
            throw  IllegalArgumentException("Insufficient numbers of items in stock")

        val productReservedEvent = reservedProductCommand.toProductReservedEvent()

        AggregateLifecycle.apply(productReservedEvent)
    }

    @CommandHandler
    fun handle(cancelProductReservationCommand: CancelProductReservationCommand){
        logger.info("CommandHandler CancelProductReservationCommand ${cancelProductReservationCommand.orderId}")

        val productReservationCancelledEvent = cancelProductReservationCommand.toProductReservationCancelledEvent()

        AggregateLifecycle.apply(productReservationCancelledEvent)
    }

    @EventSourcingHandler
    fun on(productCreatedEvent: ProductCreatedEvent){
        logger.info("EventSourcingHandler ProductCreatedEvent ${productCreatedEvent.productId}")
        productId = productCreatedEvent.productId
        price = productCreatedEvent.price
        title = productCreatedEvent.title
        quantity = productCreatedEvent.quantity
    }

    @EventSourcingHandler
    fun on(productReservedEvent: ProductReservedEvent){
        logger.info("EventSourcingHandler ProductReservedEvent ${productReservedEvent.productId}")
        quantity -= productReservedEvent.quantity
    }

    @EventSourcingHandler
    fun on(productReservationCancelledEvent: ProductReservationCancelledEvent){
        logger.info("EventSourcingHandler ProductReservationCancelledEvent ${productReservationCancelledEvent.productId}")
        quantity += productReservationCancelledEvent.quantity
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ProductAggregate::class.java)
    }

}
