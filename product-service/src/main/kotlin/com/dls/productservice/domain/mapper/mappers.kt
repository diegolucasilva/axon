package com.dls.productservice.domain.mapper

import com.dls.productservice.adapter.`in`.command.ReserveProductCommand
import com.dls.productservice.adapter.`in`.controller.ProductModel
import com.dls.productservice.adapter.command.CreateProductCommand
import com.dls.productservice.adapter.out.persistency.ProductEntity
import com.dls.productservice.adapter.out.persistency.ProductLookupEntity
import com.dls.productservice.domain.event.ProductCreatedEvent
import com.dls.productservice.domain.event.ProductReservedEvent

fun CreateProductCommand.toProductCreatedEvent(): ProductCreatedEvent {
    return ProductCreatedEvent(productId, title, price, quantity)
}

fun ReserveProductCommand.toProductReservedEvent(): ProductReservedEvent {
    return ProductReservedEvent(productId, orderId, userId,quantity )
}


fun ProductCreatedEvent.toProductEntity(): ProductEntity {
    return ProductEntity(productId, title, price, quantity)
}

fun ProductCreatedEvent.toProductLookupEntity(): ProductLookupEntity {
    return ProductLookupEntity(productId, title)
}


fun List<ProductEntity>.toProductsModel(): List<ProductModel> {
    return this.map { ProductModel(it.productId, it.title, it.price, it.quantity) }
}