package com.jyonkudo.bookmanager.feature.author.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import java.time.LocalDate

data class AuthorRegisterRequest(
    @field:NotBlank
    val name: String,
    @JsonFormat(pattern = "yyyyMMdd")
    @field:Past
    val birthDate: LocalDate
)