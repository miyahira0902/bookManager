package com.jyonkudo.bookmanager.feature.author.dto

import java.time.LocalDate

data class AuthorDto(
    val id: Int,
    val name: String,
    val birthDate: LocalDate
)
