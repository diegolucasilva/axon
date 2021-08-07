package com.dls.userservice.adapter.`in`.controller

import com.dls.userservice.domain.User
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.CompletableFuture


@RestController
@RequestMapping("/users")
class UsersQueryController(private val queryGateway: QueryGateway) {

    @GetMapping("/{userId}/payment-details")
    fun getUserPaymentDetails(@PathVariable userId: String): CompletableFuture<User> {
        val query = FetchUserPaymentDetailsQuery(UUID.fromString(userId))
        return queryGateway.query(
            FetchUserPaymentDetailsQuery(UUID.fromString(userId)),
            ResponseTypes.instanceOf(User::class.java)
        )
    }
}