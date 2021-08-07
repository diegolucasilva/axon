package com.dls.userservice.domain

import com.dls.userservice.adapter.`in`.controller.FetchUserPaymentDetailsQuery
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class UserEventsHandler {

    @QueryHandler
    fun findUserPaymentDetails(query: FetchUserPaymentDetailsQuery): User{
        val paymentDetails = PaymentDetails(
            cardNumber ="556443-33-22233",
            cvv="123",
            name="John Angry",
            validUntilMonth=12,
            validUntilYear=2030)
        return User(
            firstName="John",
            lastName="Angry",
            userId=query.userId,
            paymentDetails=paymentDetails)
    }
}