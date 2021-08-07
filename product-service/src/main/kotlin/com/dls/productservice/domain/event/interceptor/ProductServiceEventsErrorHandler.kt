package com.dls.productservice.domain.event.interceptor

import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.EventMessageHandler
import org.axonframework.eventhandling.ListenerInvocationErrorHandler
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Exception

class ProductServiceEventsErrorHandler: ListenerInvocationErrorHandler {


    override fun onError(exception: Exception?, event: EventMessage<*>?, eventHandler: EventMessageHandler?) {
       //"Used to propagate exception. it's not necessary, we can use PropagatingErrorHandler.instance())
        throw exception!!
    }
}