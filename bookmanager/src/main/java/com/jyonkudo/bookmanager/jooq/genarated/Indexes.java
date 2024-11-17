/*
 * This file is generated by jOOQ.
 */
package com.jyonkudo.bookmanager.jooq.genarated;


import com.jyonkudo.bookmanager.jooq.genarated.tables.BookAuthor;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in book_manager.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index BOOK_AUTHOR_AUTHOR_ID = Internal.createIndex(DSL.name("author_id"), BookAuthor.BOOK_AUTHOR, new OrderField[] { BookAuthor.BOOK_AUTHOR.AUTHOR_ID }, false);
}
