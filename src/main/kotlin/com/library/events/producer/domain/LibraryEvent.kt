package com.library.events.producer.domain

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
data class LibraryEvent(
    val libraryEventId: Int,
    val book: Book,
    var libraryEventType: LibraryEventType
)