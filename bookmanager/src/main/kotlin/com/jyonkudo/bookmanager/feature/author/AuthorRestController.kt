package com.jyonkudo.bookmanager.feature.author

import com.jyonkudo.bookmanager.feature.author.dto.AuthorRegisterRequest
import com.jyonkudo.bookmanager.feature.author.dto.AuthorUpdateRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/author")
class AuthorRestController(private val service: AuthorService) {

    @PostMapping()
    fun register(@Valid @RequestBody request: AuthorRegisterRequest) {
        service.register(request)
    }

    @PutMapping()
    fun update(@Valid @RequestBody request: AuthorUpdateRequest) {
        service.update(request)
    }

}

