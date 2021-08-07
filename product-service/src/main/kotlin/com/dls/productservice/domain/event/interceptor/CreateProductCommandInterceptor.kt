package com.dls.productservice.domain.event.interceptor

import com.dls.productservice.adapter.command.CreateProductCommand
import com.dls.productservice.domain.port.out.persistence.ProductLookupRepository
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.slf4j.Logger
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.function.BiFunction

@Component
class CreateProductCommandInterceptor(
    private val logger: Logger,
    private val productLookupRepository: ProductLookupRepository
): MessageDispatchInterceptor<CommandMessage<*>>{


    override fun handle(messages: List<CommandMessage<*>?>?): BiFunction<Int?, CommandMessage<*>, CommandMessage<*>>? {
        return BiFunction { _, command->
            logger.info("Intercepted commands")
            if (CreateProductCommand::class.java == command.payloadType) {
                val createProductCommand = command.payload as CreateProductCommand
                productLookupRepository.
                findByProductIdOrTitle(createProductCommand.productId, createProductCommand.title).
                ifPresent {
                    throw IllegalArgumentException("product id ${createProductCommand.productId} " +
                            "title ${createProductCommand.title} already exist")
                }

            }
             command
        }
    }
}