package com.dls.orderservice.domain.event

import com.dls.orderservice.domain.mapper.toOrderEntity
import com.dls.orderservice.domain.port.out.persistence.OrderRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("order-group")
class OrderEventsHandler(private  val orderRepository: OrderRepository) {

    @EventHandler
    fun on(event: OrderCreatedEvent) {
        val orderEntity = event.toOrderEntity()
        orderRepository.save(orderEntity)
    }
}