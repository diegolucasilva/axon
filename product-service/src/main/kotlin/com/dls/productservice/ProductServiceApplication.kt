package com.dls.productservice

import com.dls.productservice.domain.event.interceptor.CreateProductCommandInterceptor
import com.dls.productservice.domain.event.interceptor.ProductServiceEventsErrorHandler
import org.axonframework.commandhandling.CommandBus
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventsourcing.AbstractSnapshotter
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean

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

	@Bean(name= ["productSnapshotTriggerDefinition"])
	fun productSnapshotTriggerDefinition(snapshotter: Snapshotter) =
		EventCountSnapshotTriggerDefinition(snapshotter, 3)
}
fun main(args: Array<String>) {
	runApplication<ProductServiceApplication>(*args)
}

