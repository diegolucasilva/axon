package com.example.productservice.domain.port.out.persistence

import com.example.productservice.adapter.out.persistency.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository: JpaRepository<ProductEntity, UUID>