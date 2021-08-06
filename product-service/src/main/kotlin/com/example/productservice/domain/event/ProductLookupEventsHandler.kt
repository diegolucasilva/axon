package com.example.productservice.domain.event

import com.example.productservice.domain.event.query.ProductCreatedEvent
import com.example.productservice.domain.event.query.ProductsQueryHandler
import com.example.productservice.domain.mapper.toProductEntity
import com.example.productservice.domain.mapper.toProductLookupEntity
import com.example.productservice.domain.port.out.persistence.ProductLookupRepository
import com.example.productservice.domain.port.out.persistence.ProductRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("product-group")
class ProductLookupEventsHandler(private val productLookupRepository: ProductLookupRepository) {

    @EventHandler
    fun on(productCreatedEvent: ProductCreatedEvent){
        val productLookupEntity = productCreatedEvent.toProductLookupEntity()
        productLookupRepository.save(productLookupEntity)
    }
}