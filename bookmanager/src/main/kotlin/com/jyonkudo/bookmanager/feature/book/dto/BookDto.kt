package com.jyonkudo.bookmanager.feature.book.dto

import com.jyonkudo.bookmanager.jooq.genarated.enums.BookPublicationStatus

data class BookDto(
    val id: Int,
    val title: String,
    val price: Int,
    val publicationStatus: BookPublicationStatus,
)
