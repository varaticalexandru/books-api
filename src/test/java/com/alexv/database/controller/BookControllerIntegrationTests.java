package com.alexv.database.controller;

import com.alexv.database.TestDataUtil;
import com.alexv.database.domain.dto.BookDTO;
import com.alexv.database.domain.entity.BookEntity;
import com.alexv.database.mapper.Mapper;
import com.alexv.database.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookService bookService;
    private Mapper<BookEntity, BookDTO> bookMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService, Mapper<BookEntity, BookDTO> bookMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @Test
    public void testThatCreateBookSuccessfullyReturns201Created() throws Exception {

        BookDTO book = TestDataUtil.createTestBookDTO1(null);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {

        BookDTO book = TestDataUtil.createTestBookDTO1(null);
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void testThatListBooksSuccessfullyReturns200ok() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/books")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksSuccessfullyReturnsListOfBooks() throws Exception {

        BookEntity book = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/books")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(book.getTitle())
        );
    }


    @Test
    public void testThatGetBookSuccessfullyReturns200okWhenBookExists() throws Exception {

        BookEntity book = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/books/" + book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsBookWhenBookExists() throws Exception {

        BookEntity book = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/books/" + book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturns404WhenBookDoesNotExist() throws Exception {

        BookEntity book = TestDataUtil.createTestBook1(null);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/books/" + book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void TestThatUpdateBookSuccessfullyReturns200ok() throws Exception {

        BookEntity book1 = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book1.getIsbn(), book1);

        BookEntity book2 = TestDataUtil.createTestBook2(null);
        book1.setTitle(book2.getTitle());
        book1.setAuthor(book2.getAuthor());

        String bookJson = objectMapper.writeValueAsString(bookMapper.mapTo(book1));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/books/" + book1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestThatUpdateBookSuccessfullyReturnsUpdatedBook() throws Exception {

        BookEntity book1 = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book1.getIsbn(), book1);

        BookEntity book2 = TestDataUtil.createTestBook2(null);
        book1.setTitle(book2.getTitle());
        book1.setAuthor(book2.getAuthor());

        String bookJson = objectMapper.writeValueAsString(bookMapper.mapTo(book1));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/books/" + book1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book1.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book1.getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateBookReturns200ok() throws Exception {
        BookEntity book1 = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book1.getIsbn(), book1);

        BookEntity book2 = TestDataUtil.createTestBook2(null);
        book1.setTitle(book2.getTitle());
        book1.setAuthor(book2.getAuthor());

        String bookJson = objectMapper.writeValueAsString(bookMapper.mapTo(book1));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/books/" + book1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturns404WhenBookDoesNotExist() throws Exception {
        BookEntity book1 = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book1.getIsbn(), book1);

        BookEntity book2 = TestDataUtil.createTestBook2(null);
        book1.setTitle(book2.getTitle());
        book1.setAuthor(book2.getAuthor());

        String bookJson = objectMapper.writeValueAsString(bookMapper.mapTo(book1));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/books/" + "x-xxxx-xxxx-xxx-x")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception {
        BookEntity book1 = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book1.getIsbn(), book1);

        BookEntity book2 = TestDataUtil.createTestBook2(null);
        book1.setTitle(book2.getTitle());
        book1.setAuthor(book2.getAuthor());

        String bookJson = objectMapper.writeValueAsString(bookMapper.mapTo(book1));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/books/" + book1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book1.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book1.getIsbn())
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204NoContentForExistingBook() throws Exception {
        BookEntity book1 = TestDataUtil.createTestBook1(null);
        bookService.saveBook(book1.getIsbn(), book1);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/books/" + book1.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204NoContentForNonExistingBook() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/books/" + "99")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
