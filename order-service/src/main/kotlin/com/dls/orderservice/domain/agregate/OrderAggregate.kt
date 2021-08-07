package com.dls.orderservice.domain.agregate

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import com.dls.orderservice.domain.event.OrderCreatedEvent
import com.dls.orderservice.domain.mapper.toOrderCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.properties.Delegates


@Aggregate
class OrderAggregate {
    @AggregateIdentifier
    private lateinit var orderId: UUID
    private lateinit var userId: UUID
    private lateinit var productId: UUID
    private lateinit var addressId: String
    private var quantity by Delegates.notNull<Int>()
    private lateinit var orderStatus: CreateOrderCommand.OrderStatus


    @CommandHandler
    constructor(createOrderCommand: CreateOrderCommand){
        val orderCreatedEvent = createOrderCommand.toOrderCreatedEvent()
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    fun on(orderCreatedEvent: OrderCreatedEvent) {
        orderId = orderCreatedEvent.orderId
        productId = orderCreatedEvent.productId
        userId = orderCreatedEvent.userId
        addressId = orderCreatedEvent.addressId
        quantity = orderCreatedEvent.quantity
        orderStatus = orderCreatedEvent.orderStatus
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderAggregate::class.java)
    }

}
