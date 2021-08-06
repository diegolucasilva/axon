package com.example.productservice.adapter.`in`.controller

import java.util.*

data class ProductModel(
     val productId: UUID,
     val title:String,
     val price:Double,
     val quantity: Int)