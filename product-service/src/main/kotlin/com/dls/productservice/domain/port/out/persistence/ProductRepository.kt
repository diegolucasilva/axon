package com.dls.productservice.domain.port.out.persistence

import com.dls.productservice.adapter.out.persistency.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository: JpaRepository<ProductEntity, UUID>