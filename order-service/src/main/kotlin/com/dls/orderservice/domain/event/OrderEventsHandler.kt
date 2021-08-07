package com.dls.orderservice.domain.event

import com.dls.orderservice.domain.mapper.toOrderEntity
import com.dls.orderservice.domain.port.out.persistence.OrderRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("order-group")
class OrderEventsHandler(private  val orderRepository: OrderRepository) {

    @EventHandler
    fun on(event: OrderCreatedEvent) {
        logger.info("OrderEventsHandler OrderCreatedEvent to order ${event.orderId}")
        val orderEntity = event.toOrderEntity()
        orderRepository.save(orderEntity)
    }

    @EventHandler
    fun on(event: OrderApprovedEvent) {
        logger.info("OrderApprovedEvent OrderApprovedEvent to order ${event.orderId}")
        orderRepository.findById(event.orderId).ifPresent {
            it.orderStatus = event.orderStatus
            orderRepository.save(it)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderEventsHandler::class.java)
    }
}