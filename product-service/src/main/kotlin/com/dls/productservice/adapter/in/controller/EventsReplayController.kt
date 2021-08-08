package com.dls.productservice.adapter.`in`.controller

import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/management")
class EventsReplayController(private val eventProcessingConfiguration: EventProcessingConfiguration) {

    @PostMapping("eventProcessor/{processorName}/reset")
    fun replayEvents(@PathVariable processorName: String): ResponseEntity<String> {
        val trackingEventProcessor = eventProcessingConfiguration.
        eventProcessor(processorName,TrackingEventProcessor::class.java)

        if(trackingEventProcessor.isPresent) {
            trackingEventProcessor.get().shutDown()
            trackingEventProcessor.get().resetTokens()
            trackingEventProcessor.get().start()
            return ResponseEntity.ok().body("The event ${trackingEventProcessor.get().name} has been reset")
        }
        return ResponseEntity.badRequest().body("The event processor ${trackingEventProcessor.get().name} is not a tracking event processor. Not supported")

    }
}