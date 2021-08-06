package com.dls.orderservice.adapter.`in`.controller

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CreateOrderRequest(
    @field:NotBlank(message = "Order productId is a required field")
    val productId: String,
    @field:Min(value = 1, message = "Quantity cannot be lower than 1")
    @field:Max(value = 5, message = "Quantity cannot be larger than 5")
    val quantity: Int,
    @field:NotBlank(message = "Order addressId is a required field")
    val addressId: String
)