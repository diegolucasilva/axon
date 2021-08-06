/*
package com.example.productservice.config

import com.example.productservice.domain.event.interceptor.CreateProductCommandInterceptor
import org.axonframework.commandhandling.CommandBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
class CommandBusConfiguration {

    @Autowired
    fun commandBus(context: ApplicationContext, commandBus: CommandBus): CommandBus{
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor::class.java))
    }
}*/
