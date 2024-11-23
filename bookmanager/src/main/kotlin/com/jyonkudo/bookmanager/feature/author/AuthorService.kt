package com.jyonkudo.bookmanager.feature.author

import com.jyonkudo.bookmanager.feature.author.dto.AuthorRegisterRequest
import com.jyonkudo.bookmanager.feature.author.dto.AuthorUpdateRequest
import com.jyonkudo.bookmanager.jooq.genarated.tables.Author.AUTHOR
import com.jyonkudo.bookmanager.jooq.genarated.tables.records.AuthorRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class AuthorService(private val create: DSLContext) {

    fun register(request: AuthorRegisterRequest) {
        val record: AuthorRecord = create.newRecord(AUTHOR)
        record.name = request.name
        record.birthDate = request.birthDate
        record.store()
    }

    fun update(request: AuthorUpdateRequest) {
        val mayNullRecord: AuthorRecord? = create.fetchOne(AUTHOR, AUTHOR.ID.eq(request.id))
        val record = mayNullRecord ?: throw IllegalArgumentException("Record not found")

        record.name = request.name
        record.birthDate = request.birthDate
        record.store()
    }
}