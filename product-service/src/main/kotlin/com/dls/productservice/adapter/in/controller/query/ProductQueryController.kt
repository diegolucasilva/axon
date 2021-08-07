package com.dls.productservice.adapter.`in`.controller.query

import com.dls.productservice.adapter.`in`.command.FindProductsQuery
import com.dls.productservice.adapter.`in`.controller.ProductModel
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/products")
class ProductQueryController(private val queryGateway: QueryGateway) {

    @GetMapping
    fun getProducts(): CompletableFuture<List<ProductModel>> {
        val findProductsQuery = FindProductsQuery()
        return queryGateway.query(
            findProductsQuery,
            ResponseTypes.multipleInstancesOf(ProductModel::class.java)
        )
    }
}