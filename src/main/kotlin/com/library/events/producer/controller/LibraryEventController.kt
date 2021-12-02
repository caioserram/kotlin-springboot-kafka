package com.library.events.producer.controller

import com.library.events.producer.domain.LibraryEvent
import com.library.events.producer.producer.LibraryEventProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value=["/v1/libraryevent"])
class LibraryEventController {

    @Autowired
    lateinit var libraryEventProducer: LibraryEventProducer

    var log: Logger = LoggerFactory.getLogger("BookEventController")

    @PostMapping()
    fun createBookEvent(@RequestBody libraryEvent: LibraryEvent): ResponseEntity<LibraryEvent>{
        log.info("Before send library event")
        // Due to its asynchronous behavior, method will be considered successful even though the message did not get to
        // the broker
//        libraryEventProducer.sendLibraryEventAsync(libraryEvent)
//        val sendResult = libraryEventProducer.sendLibraryEvent(libraryEvent)
//        log.info("SendResult is ${sendResult.toString()}")
        libraryEventProducer.sendLibraryEventAsyncConfiguration(libraryEvent)
        log.info("After send library event")
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent)
    }
}