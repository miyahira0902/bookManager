package com.jyonkudo.bookmanager.feature.book.dto

import com.jyonkudo.bookmanager.feature.author.dto.AuthorDto
import com.jyonkudo.bookmanager.jooq.genarated.enums.BookPublicationStatus

data class BookSearchResponse(
    val book: BookDto,
    val authorList: List<AuthorDto>
)
