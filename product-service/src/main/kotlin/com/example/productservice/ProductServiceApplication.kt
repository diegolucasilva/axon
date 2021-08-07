package com.example.productservice

import com.example.productservice.domain.event.interceptor.CreateProductCommandInterceptor
import com.example.productservice.domain.event.interceptor.ProductServiceEventsErrorHandler
import org.axonframework.commandhandling.CommandBus
import org.axonframework.config.Configuration
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class ProductServiceApplication{

	@Autowired
	fun registerCreateProductCommandInterceptor(context: ApplicationContext, commandBus: CommandBus){
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor::class.java))
	}

	@Autowired
	fun eventProcessingConfigurer(config: EventProcessingConfigurer){
		//config.registerListenerInvocationErrorHandler("product-group", {conf -> PropagatingErrorHandler.instance()})
		//config.registerListenerInvocationErrorHandler("product-group") { PropagatingErrorHandler.instance() }
		config.registerListenerInvocationErrorHandler("products-group") { ProductServiceEventsErrorHandler() }
	}
}
fun main(args: Array<String>) {
	runApplication<ProductServiceApplication>(*args)
}

