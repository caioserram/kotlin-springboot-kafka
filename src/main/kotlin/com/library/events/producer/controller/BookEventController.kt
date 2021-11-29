package com.library.events.producer.controller

import com.library.events.producer.domain.Book
import com.library.events.producer.domain.LibraryEvent
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value=["/v1/libraryevent"])
class BookEventController {

    @PostMapping()
    fun createBookEvent(@RequestBody libraryEvent: LibraryEvent): ResponseEntity<LibraryEvent>{
        print(libraryEvent)
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent)
    }
}