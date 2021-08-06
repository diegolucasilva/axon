package com.example.productservice.domain.event.query

import com.example.productservice.adapter.`in`.command.FindProductsQuery
import com.example.productservice.adapter.`in`.controller.ProductModel
import com.example.productservice.domain.mapper.toProductsModel
import com.example.productservice.domain.port.out.persistence.ProductRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductsQueryHandler(private val productRepository: ProductRepository) {


    @QueryHandler
    fun findProducts(findProductsQuery: FindProductsQuery): List<ProductModel>{
        val productsEntity = productRepository.findAll()
        return productsEntity.toProductsModel()
    }

}