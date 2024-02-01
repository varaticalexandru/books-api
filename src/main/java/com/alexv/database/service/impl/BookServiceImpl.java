package com.alexv.database.service.impl;

import com.alexv.database.domain.entity.BookEntity;
import com.alexv.database.repository.BookRepository;
import com.alexv.database.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity saveBook(String isbn, BookEntity bookEntity) {

        bookEntity.setIsbn(isbn);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        return savedBookEntity;
    }

    @Override
    public List<BookEntity> listBooks() {

        List<BookEntity> books = StreamSupport.stream(bookRepository.findAll()
                 .spliterator(),
                 false)
                 .toList();

        return books;
    }

    @Override
    public Page<BookEntity> listBooks(Pageable pageable) {

        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> getBook(String isbn) {

        Optional<BookEntity> book = bookRepository.findById(isbn);
        return book;
    }

    @Override
    public Boolean isExists(String isbn) {

        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {

        bookEntity.setIsbn(isbn);
        return bookRepository.findById(isbn).map(
                existingBook -> {
                    Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
                    return bookRepository.save(existingBook);
                }
        ).orElseThrow(() -> new RuntimeException("Book does not exist."));
    }

    @Override
    public void deleteBook(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
