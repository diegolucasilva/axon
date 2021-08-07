package com.dls.productservice.adapter.`in`.controller

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CreateProductRequest(
     @field:NotBlank(message = "field required")
     val title:String,
     val price:Double,
     @field:Min(value=1, message="cannot be lower than 1")
     @field:Max(value=5, message="cannot be larger than 5")
     val quantity: Int)