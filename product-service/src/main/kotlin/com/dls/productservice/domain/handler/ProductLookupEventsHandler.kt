package com.dls.productservice.domain.handler

import com.dls.productservice.domain.aggregate.ProductAggregate
import com.dls.productservice.domain.event.ProductCreatedEvent
import com.dls.productservice.domain.mapper.toProductLookupEntity
import com.dls.productservice.domain.port.out.persistence.ProductLookupRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("product-group")
class ProductLookupEventsHandler(private val productLookupRepository: ProductLookupRepository) {

    @EventHandler
    fun on(productCreatedEvent: ProductCreatedEvent){
        logger.info("EventHandler ProductCreatedEvent ${productCreatedEvent.productId}")

        val productLookupEntity = productCreatedEvent.toProductLookupEntity()
        productLookupRepository.save(productLookupEntity)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ProductLookupEventsHandler::class.java)
    }
}