package com.dls.productservice.domain.handler

import com.dls.productservice.domain.event.ProductCreatedEvent
import com.dls.productservice.domain.mapper.toProductLookupEntity
import com.dls.productservice.domain.port.out.persistence.ProductLookupRepository
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