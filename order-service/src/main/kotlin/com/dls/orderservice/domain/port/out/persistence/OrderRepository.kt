package com.dls.orderservice.domain.port.out.persistence

import com.dls.orderservice.adapter.out.persistency.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository: JpaRepository<OrderEntity, UUID>