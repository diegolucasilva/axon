package com.dls.userservice.domain

import java.util.*

class User(
     val firstName: String,
     val lastName: String,
     val userId: UUID,
     val paymentDetails: PaymentDetails
)