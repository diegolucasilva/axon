package com.dls.orderservice.domain.agregate

import com.dls.orderservice.adapter.`in`.command.ApproveOrderCommand
import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import com.dls.orderservice.adapter.`in`.command.RejectOrderCommand
import com.dls.orderservice.domain.event.OrderApprovedEvent
import com.dls.orderservice.domain.event.OrderCreatedEvent
import com.dls.orderservice.domain.event.OrderRejectedEvent
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
class OrderAggregate() {
    @AggregateIdentifier
    private lateinit var orderId: UUID
    private lateinit var userId: UUID
    private lateinit var productId: UUID
    private lateinit var addressId: String
    private var quantity by Delegates.notNull<Int>()
    private lateinit var orderStatus: CreateOrderCommand.OrderStatus


    @CommandHandler
    constructor(createOrderCommand: CreateOrderCommand) : this() {
        logger.info("CommandHandler CreateOrderCommand to order ${createOrderCommand.orderId}")
        val orderCreatedEvent = createOrderCommand.toOrderCreatedEvent()
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @CommandHandler
    fun handle(approveOrderCommand: ApproveOrderCommand){
        logger.info("CommandHandler ApproveOrderCommand to order ${approveOrderCommand.orderId}")
        val orderApprovedEvent = OrderApprovedEvent(approveOrderCommand.orderId)
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @CommandHandler
    fun handle(rejectOrderCommand: RejectOrderCommand){
        logger.info("CommandHandler RejectOrderCommand to order ${rejectOrderCommand.orderId}")
        val orderRejectedEvent = OrderRejectedEvent(orderId =rejectOrderCommand.orderId, reason =rejectOrderCommand.reason)
        AggregateLifecycle.apply(orderRejectedEvent);
    }

    @EventSourcingHandler
    fun on(orderCreatedEvent: OrderCreatedEvent) {
        logger.info("EventSourcingHandler OrderCreatedEvent to order ${orderCreatedEvent.orderId}")
        orderId = orderCreatedEvent.orderId
        productId = orderCreatedEvent.productId
        userId = orderCreatedEvent.userId
        addressId = orderCreatedEvent.addressId
        quantity = orderCreatedEvent.quantity
        orderStatus = orderCreatedEvent.orderStatus
    }

    @EventSourcingHandler
    fun on(orderApprovedEvent: OrderApprovedEvent) {
        logger.info("EventSourcingHandler OrderApprovedEvent to order ${orderApprovedEvent.orderId}")
        orderStatus = orderApprovedEvent.orderStatus
    }

    @EventSourcingHandler
    fun on(orderRejectedEvent: OrderRejectedEvent) {
        logger.info("EventSourcingHandler OrderRejectedEvent to order ${orderRejectedEvent.orderId}")
        orderStatus = orderRejectedEvent.orderStatus
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderAggregate::class.java)
    }

}
