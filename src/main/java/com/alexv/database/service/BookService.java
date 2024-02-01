package com.alexv.database.service;

import com.alexv.database.domain.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookEntity saveBook(String isbn, BookEntity bookEntity);
    List<BookEntity> listBooks();
    Page<BookEntity> listBooks(Pageable pageable);
    Optional<BookEntity> getBook(String isbn);
    Boolean isExists (String isbn);
    BookEntity partialUpdate(String isbn, BookEntity bookEntity);
    void deleteBook(String isbn);
}
