package com.example.productservice.domain.handler

import com.example.productservice.domain.event.ProductCreatedEvent
import com.example.productservice.domain.mapper.toProductLookupEntity
import com.example.productservice.domain.port.out.persistence.ProductLookupRepository
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