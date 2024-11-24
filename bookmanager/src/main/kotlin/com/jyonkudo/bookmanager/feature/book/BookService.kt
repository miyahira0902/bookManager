package com.jyonkudo.bookmanager.feature.book

import com.jyonkudo.bookmanager.feature.book.dto.BookRegisterRequest
import com.jyonkudo.bookmanager.feature.book.dto.BookUpdateRequest
import com.jyonkudo.bookmanager.jooq.genarated.enums.BookPublicationStatus
import com.jyonkudo.bookmanager.jooq.genarated.tables.Book.BOOK
import com.jyonkudo.bookmanager.jooq.genarated.tables.BookAuthor.BOOK_AUTHOR
import com.jyonkudo.bookmanager.jooq.genarated.tables.records.BookAuthorRecord
import com.jyonkudo.bookmanager.jooq.genarated.tables.records.BookRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(private val dslContext: DSLContext) {

    fun register(request: BookRegisterRequest) {

        // 書籍を登録
        val bookRecord: BookRecord = dslContext.newRecord(BOOK)
        bookRecord.title = request.title
        bookRecord.price = request.price
        bookRecord.publicationStatus = request.publicationStatus
        bookRecord.store()

        // 書籍-著者を登録
        request.authorIdList.forEach { authorId ->
            val bookAuthorRecord: BookAuthorRecord = dslContext.newRecord(BOOK_AUTHOR)
            bookAuthorRecord.bookId = bookRecord.id
            bookAuthorRecord.authorId = authorId
            bookAuthorRecord.store()
        }
    }

    fun update(request: BookUpdateRequest) {

        // 書籍を更新
        val mayNullRecord: BookRecord? = dslContext.fetchOne(BOOK, BOOK.ID.eq(request.id))
        val bookRecord = mayNullRecord ?: throw IllegalArgumentException("Book not found")

        bookRecord.title = request.title
        bookRecord.price = request.price
        bookRecord.publicationStatus = request.publicationStatus
        bookRecord.store()

        // 書籍-著者を更新（洗い替え）
        dslContext
            .deleteFrom(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.BOOK_ID.eq(request.id))
            .execute()

        request.authorIdList.forEach { authorId ->
            val bookAuthorRecord: BookAuthorRecord = dslContext.newRecord(BOOK_AUTHOR)
            bookAuthorRecord.bookId = bookRecord.id
            bookAuthorRecord.authorId = authorId
            bookAuthorRecord.store()
        }
    }

    fun isValid(request: BookUpdateRequest): Boolean {

        val mayNullRecord: BookRecord? = dslContext.fetchOne(BOOK, BOOK.ID.eq(request.id))
        val bookRecord = mayNullRecord ?: throw IllegalArgumentException("Book not found")

        return bookRecord.publicationStatus == BookPublicationStatus.PUBLISHED &&
                request.publicationStatus == BookPublicationStatus.NOT_PUBLISHED

    }


}