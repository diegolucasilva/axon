package com.dls.paymentservice.adapter.`in`.command

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/payments")
class PaymentController(private val commandGateway: CommandGateway) {

    @PostMapping
    fun createProduct(){
        val processPaymentCommand = ProcessPaymentCommand(
            paymentId = UUID.randomUUID(),
            orderId =  UUID.randomUUID(),
            paymentDetails= ProcessPaymentCommand.PaymentDetails(
                cardNumber ="123",
                cvv= "123",
                name= "John",
                validUntilMonth=10,
                validUntilYear=10)
        )
        commandGateway.sendAndWait<ProcessPaymentCommand>(processPaymentCommand, 10, TimeUnit.SECONDS)
    }

}
