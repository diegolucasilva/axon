package com.dls.productservice.domain.event.query

import com.dls.productservice.domain.aggregate.ProductAggregate
import com.dls.productservice.domain.event.ProductCreatedEvent
import com.dls.productservice.domain.event.ProductReservationCancelledEvent
import com.dls.productservice.domain.event.ProductReservedEvent
import com.dls.productservice.domain.mapper.toProductEntity
import com.dls.productservice.domain.port.out.persistence.ProductRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.messaging.interceptors.ExceptionHandler
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
@ProcessingGroup("product-group")
class ProductsEventsHandler(private val productRepository: ProductRepository) {


    @ExceptionHandler(resultType = Exception::class)
    fun handle(exception: Exception){
        logger.error("ExceptionHandler ExceptionHandler")

        //log error, treat exception, rollback transaction. Only exceptions throws by this class
        /*
            if axon.eventhandling.processors.product-group.mode=subscribing
            is active, it is possible to rollback the entire transaction (event store, db)(
            be process in the same thread). To do this, we can't handle exceptions here. Only we need it is propagate
         */
        throw exception
    }

    @ExceptionHandler(resultType = IllegalArgumentException::class)
    fun handle(exception: IllegalArgumentException){
        logger.error("ExceptionHandler IllegalArgumentException")

        //log error, treat exception, rollback transaction. Only exceptions throws by this class
    }

    @EventHandler
    fun on(productCreatedEvent: ProductCreatedEvent){
        logger.info("EventHandler ProductCreatedEvent ${productCreatedEvent.productId}")
        val productEntity = productCreatedEvent.toProductEntity()
        productRepository.save(productEntity)

        //throw Exception("Forcing exception in the Event handler to see the rollback process")
    }

    @EventHandler
    fun on(productReservedEvent: ProductReservedEvent){
        logger.info("EventHandler ProductReservedEvent ${productReservedEvent.productId}")
        productRepository.findById(productReservedEvent.productId).ifPresent{
            it.quantity -= productReservedEvent.quantity
            productRepository.save(it)
        }
    }

    @EventHandler
    fun on(productReservationCancelledEvent: ProductReservationCancelledEvent){
        logger.info("EventHandler ProductReservationCancelledEvent ${productReservationCancelledEvent.productId}")
        productRepository.findById(productReservationCancelledEvent.productId).ifPresent{
            it.quantity += productReservationCancelledEvent.quantity
            productRepository.save(it)
        }
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(ProductsEventsHandler::class.java)
    }
}