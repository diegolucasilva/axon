package com.dls.orderservice.domain.event

import com.dls.orderservice.adapter.out.persistency.OrderEntity
import com.dls.orderservice.domain.port.out.persistence.OrderRepository
import com.dls.orderservice.domain.query.FindOrderQuery
import com.dls.orderservice.domain.query.OrderSummary
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class OrderQueriesHandler(private  val orderRepository: OrderRepository) {

    @QueryHandler
    fun findOrder(findOrderQuery: FindOrderQuery): OrderSummary{
        val orderEntity = orderRepository.findById(findOrderQuery.orderId)
        return OrderSummary(orderEntity.get().orderId, orderEntity.get().orderStatus)
    }

}
