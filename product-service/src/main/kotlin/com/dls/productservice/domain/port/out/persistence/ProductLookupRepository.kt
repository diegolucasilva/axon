package com.dls.productservice.domain.port.out.persistence

import com.dls.productservice.adapter.out.persistency.ProductLookupEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductLookupRepository: JpaRepository<ProductLookupEntity, UUID>{

   fun findByProductIdOrTitle(productId:UUID,title: String): Optional<ProductLookupEntity>
}