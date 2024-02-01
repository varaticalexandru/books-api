package com.alexv.database.controller;

import com.alexv.database.domain.dto.BookDTO;
import com.alexv.database.domain.entity.BookEntity;
import com.alexv.database.mapper.Mapper;
import com.alexv.database.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.modelmapper.Converters.Collection.map;

@RestController
public class BookController {

    private BookService bookService;
    private Mapper<BookEntity, BookDTO> bookMapper;

    @Autowired
    public BookController(BookService bookService, Mapper<BookEntity, BookDTO> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDTO> createUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDTO book) {

        Boolean isExists = bookService.isExists(isbn);

        BookEntity bookEntity = bookMapper.mapFrom(book);
        BookEntity savedBookEntity = bookService.saveBook(isbn, bookEntity);
        BookDTO savedBookDTO = bookMapper.mapTo(savedBookEntity);

        if (isExists) {
            // update
            return new ResponseEntity<>(savedBookDTO, HttpStatus.OK);
        }
        else {
            // create
            return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/books")
    public ResponseEntity<Page<BookDTO>> listBooks(Pageable pageable) {

        Page<BookEntity> books =  bookService.listBooks(pageable);
        Page<BookDTO> bookDTOS = books.map(bookMapper::mapTo);

        return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> getBook (@PathVariable("isbn") String isbn) {

        Optional<BookEntity> foundBook = bookService.getBook(isbn);

        return foundBook.map(
                bookEntity -> {
                    BookDTO book = bookMapper.mapTo(bookEntity);
                    return new ResponseEntity<>(book, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{id}")
    public ResponseEntity<BookEntity> partialUpdate(
            @PathVariable("id") String isbn,
            @RequestBody BookDTO book) {

        if (!bookService.isExists(isbn))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        BookEntity bookEntity = bookMapper.mapFrom(book);
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);

        return new ResponseEntity<>(updatedBookEntity, HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {

        bookService.deleteBook(isbn);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
