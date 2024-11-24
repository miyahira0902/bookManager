package com.jyonkudo.bookmanager.feature.book

import com.jyonkudo.bookmanager.feature.book.dto.BookRegisterRequest
import com.jyonkudo.bookmanager.feature.book.dto.BookUpdateRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/book")
class BookRestController(private val service: BookService) {

    @PostMapping()
    fun register(@Valid @RequestBody request: BookRegisterRequest) {
        service.register(request)
    }

    @PutMapping()
    fun update(@Valid @RequestBody request: BookUpdateRequest) {

        if (!service.isValid(request)) {
            throw IllegalArgumentException("出版済みから未出版に変更することはできません。");
        }

        service.update(request)
    }
}