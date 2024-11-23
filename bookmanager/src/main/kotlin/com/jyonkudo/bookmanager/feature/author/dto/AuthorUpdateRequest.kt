package com.jyonkudo.bookmanager.feature.author.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import java.time.LocalDate

data class AuthorUpdateRequest(
    @field:NotNull
    val id: Int,
    @field:NotBlank
    val name: String,
    @JsonFormat(pattern = "yyyyMMdd")
    @field:Past
    val birthDate: LocalDate
)