package com.dls.orderservice.adapter.`in`.controller

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import com.dls.orderservice.domain.query.FindOrderQuery
import com.dls.orderservice.domain.query.OrderSummary
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.SubscriptionQueryResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/orders")
class OrdersCommandController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway) {


    @PostMapping
    fun createProduct(@Valid @RequestBody createOrderRequest: CreateOrderRequest): OrderSummary? {

        val createOrderCommand = CreateOrderCommand(
            orderId = UUID.randomUUID(),
            userId = UUID.fromString("27b95829-4f3f-4ddf-8983-151ba010e35b"),
            productId = UUID.fromString(createOrderRequest.productId),
            addressId = createOrderRequest.addressId,
            quantity = createOrderRequest.quantity,
            orderStatus = CreateOrderCommand.OrderStatus.CREATED
        )
        val queryResult: SubscriptionQueryResult<OrderSummary,OrderSummary> = queryGateway.subscriptionQuery(FindOrderQuery(createOrderCommand.orderId),
            ResponseTypes.instanceOf(OrderSummary::class.java),
            ResponseTypes.instanceOf(OrderSummary::class.java))
         commandGateway.sendAndWait<CreateOrderCommand>(createOrderCommand)

        queryResult.use { queryResult ->
            return queryResult.updates().blockFirst()
        }

    }
}