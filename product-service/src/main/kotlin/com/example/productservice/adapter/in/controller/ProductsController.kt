package com.example.productservice.adapter.`in`.controller
import com.example.productservice.adapter.`in`.command.CreateProductCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid


@RestController
class ProductsController(private val commandGateway: CommandGateway) {

    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequest){
        val createProductCommand = CreateProductCommand(
            price = request.price,
            quantity = request.quantity,
            title = request.title,
            productId = UUID.randomUUID()
        )
        commandGateway.sendAndWait<CreateProductCommand>(createProductCommand)
    }

}
