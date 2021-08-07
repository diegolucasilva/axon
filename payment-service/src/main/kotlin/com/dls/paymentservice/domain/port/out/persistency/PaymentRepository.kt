package com.dls.paymentservice.domain.port.out.persistency

import com.dls.paymentservice.adapter.out.persistency.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentRepository : JpaRepository<PaymentEntity, UUID>