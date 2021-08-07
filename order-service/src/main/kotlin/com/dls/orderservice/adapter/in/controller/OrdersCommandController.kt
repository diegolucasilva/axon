package com.dls.orderservice.adapter.`in`.controller

import com.dls.orderservice.adapter.`in`.command.CreateOrderCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/orders")
class OrdersCommandController(private val commandGateway: CommandGateway) {


    @PostMapping
    fun createProduct(@Valid @RequestBody createOrderRequest: CreateOrderRequest){

        val createOrderCommand = CreateOrderCommand(
            orderId = UUID.randomUUID(),
            userId = UUID.fromString("27b95829-4f3f-4ddf-8983-151ba010e35b"),
            productId = UUID.fromString(createOrderRequest.productId),
            addressId = createOrderRequest.addressId,
            quantity = createOrderRequest.quantity,
            orderStatus = CreateOrderCommand.OrderStatus.CREATED
        )
        commandGateway.sendAndWait<CreateOrderCommand>(createOrderCommand)
    }
}