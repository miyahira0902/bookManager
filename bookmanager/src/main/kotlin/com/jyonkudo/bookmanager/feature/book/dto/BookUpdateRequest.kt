package com.jyonkudo.bookmanager.feature.book.dto

import com.jyonkudo.bookmanager.jooq.genarated.enums.BookPublicationStatus
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BookUpdateRequest(
    @field:NotNull
    val id: Int,
    val title: String,
    @field:Min(0)
    val price: Int,
    val publicationStatus: BookPublicationStatus,
    @field:Size(min = 1)
    val authorIdList: List<Int>
)