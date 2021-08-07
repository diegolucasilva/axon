package com.dls.productservice.domain.event.query

import com.dls.productservice.adapter.`in`.command.FindProductsQuery
import com.dls.productservice.adapter.`in`.controller.ProductModel
import com.dls.productservice.domain.mapper.toProductsModel
import com.dls.productservice.domain.port.out.persistence.ProductRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductsQueryHandler(private val productRepository: ProductRepository) {


    @QueryHandler
    fun findProducts(findProductsQuery: FindProductsQuery): List<ProductModel>{
        logger.info("QueryHandler FindProductsQuery")
        val productsEntity = productRepository.findAll()
        return productsEntity.toProductsModel()
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(ProductsQueryHandler::class.java)
    }

}