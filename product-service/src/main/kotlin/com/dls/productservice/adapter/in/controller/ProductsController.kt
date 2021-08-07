package com.dls.productservice.adapter.`in`.controller
import com.dls.productservice.adapter.`in`.command.ReserveProductCommand
import com.dls.productservice.adapter.command.CreateProductCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/products")
class ProductsController(private val commandGateway: CommandGateway) {

    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequest){
        val createProductCommand = CreateProductCommand(
            price = request.price,
            quantity = request.quantity,
            title = request.title,
            productId = UUID.randomUUID()
        )

      /*  val reserveProductCommand = ReserveProductCommand.
        builder().
        productId(UUID.fromString("728add14-546d-449a-a4bd-69a59a25b77e")).
        orderId(UUID.randomUUID()).
        userId(UUID.randomUUID()).
        quantity(1).
        build()
        val reserveProductCommand = ReserveProductCommand(
            productId= UUID.fromString("728add14-546d-449a-a4bd-69a59a25b77e"),
            orderId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            quantity = 1
            )*/
        commandGateway.sendAndWait<ReserveProductCommand>(createProductCommand)
    }

}
