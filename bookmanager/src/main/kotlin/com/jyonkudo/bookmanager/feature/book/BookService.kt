package com.jyonkudo.bookmanager.feature.book

import com.jyonkudo.bookmanager.feature.author.dto.AuthorDto
import com.jyonkudo.bookmanager.feature.book.dto.*
import com.jyonkudo.bookmanager.jooq.genarated.enums.BookPublicationStatus
import com.jyonkudo.bookmanager.jooq.genarated.tables.Author.AUTHOR
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

    fun searchByAuthorId(authorId: Int): List<BookSearchResponse> {

        // 著者に紐づく書籍を取得
        val bookList = dslContext.select(BOOK)
            .from(BOOK)
            .join(BOOK_AUTHOR).on(BOOK.ID.eq(BOOK_AUTHOR.BOOK_ID))
            .join(AUTHOR).on(AUTHOR.ID.eq(BOOK_AUTHOR.AUTHOR_ID))
            .where(AUTHOR.ID.eq(authorId))
            .fetchArray()

        // 各書籍の著者を取得
        val authorList = dslContext.select(BOOK_AUTHOR.`as`("ba1").BOOK_ID, AUTHOR.ID, AUTHOR.NAME, AUTHOR.BIRTH_DATE)
            .from(BOOK_AUTHOR.`as`("ba1"))
            .join(BOOK_AUTHOR.`as`("ba2"))
            .on(BOOK_AUTHOR.`as`("ba1").BOOK_ID.eq(BOOK_AUTHOR.`as`("ba2").BOOK_ID))
            .join(AUTHOR)
            .on(BOOK_AUTHOR.`as`("ba2").AUTHOR_ID.eq(AUTHOR.ID))
            .where(BOOK_AUTHOR.`as`("ba1").AUTHOR_ID.eq(1))
            .fetchArray();

        // 書籍と著者リストをマッピング
        val bookSearchResponseList = bookList.map { wrappedBookRecord ->
            val bookRecord = wrappedBookRecord.component1()

            // 該当書籍の著者リストを抽出
            val authorsOfBook = authorList.filter { authorRecord ->
                authorRecord[BOOK_AUTHOR.BOOK_ID] == bookRecord[BOOK.ID]
            }.map { authorRecord ->
                AuthorDto(
                    id = authorRecord[AUTHOR.ID],
                    name = authorRecord[AUTHOR.NAME],
                    birthDate = authorRecord[AUTHOR.BIRTH_DATE]
                )
            }

            BookSearchResponse(
                book = BookDto(
                    id = bookRecord[BOOK.ID],
                    title = bookRecord[BOOK.TITLE],
                    price = bookRecord[BOOK.PRICE],
                    publicationStatus = bookRecord[BOOK.PUBLICATION_STATUS]
                ),
                authorList = authorsOfBook
            )
        }

        return bookSearchResponseList
    }


}