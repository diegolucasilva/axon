package com.example.productservice.domain.mapper

import com.example.productservice.adapter.`in`.command.CreateProductCommand
import com.example.productservice.adapter.`in`.controller.ProductModel
import com.example.productservice.adapter.out.persistency.ProductEntity
import com.example.productservice.adapter.out.persistency.ProductLookupEntity
import com.example.productservice.domain.event.query.ProductCreatedEvent


fun CreateProductCommand.toProductCreatedEvent(): ProductCreatedEvent {
    return ProductCreatedEvent(productId, title, price, quantity)
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