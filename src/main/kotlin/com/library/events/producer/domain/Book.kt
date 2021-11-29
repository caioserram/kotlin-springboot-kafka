package com.library.events.producer.domain

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
data class Book (
    val bookId: Int,
    val bookName: String,
    val bookAuthor: String
    )
