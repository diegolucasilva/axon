package com.dls.orderservice.domain.mapper

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import com.dls.orderservice.adapter.out.persistency.OrderEntity
import com.dls.orderservice.domain.event.OrderCreatedEvent


fun CreateOrderCommand.toOrderCreatedEvent(): OrderCreatedEvent {
    return OrderCreatedEvent(orderId, userId, productId, addressId,quantity, orderStatus)
}

fun OrderCreatedEvent.toOrderEntity(): OrderEntity {
    return OrderEntity(orderId, userId, productId, addressId,quantity, orderStatus)
}