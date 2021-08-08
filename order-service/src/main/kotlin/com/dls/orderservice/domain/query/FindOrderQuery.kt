package com.dls.orderservice.domain.query

import java.util.*

data class FindOrderQuery(
    val orderId: UUID
)