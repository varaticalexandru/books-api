package com.alexv.database.controller;

import com.alexv.database.TestDataUtil;
import com.alexv.database.domain.dto.AuthorDTO;
import com.alexv.database.domain.entity.AuthorEntity;
import com.alexv.database.mapper.Mapper;
import com.alexv.database.mapper.impl.AuthorMapperImpl;
import com.alexv.database.service.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class AuthorControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AuthorService authorService;
    private AuthorMapperImpl authorMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService, AuthorMapperImpl authorMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @Test
    public void testThatCreateAuthorReturnsHttpStatus201Created() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor1();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorReturnsSavedAuthor() throws Exception {

        AuthorDTO author = TestDataUtil.createTestAuthorDTO1();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200ok() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/authors")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/authors")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(author.getAge())
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/authors/99")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExists() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception {

        AuthorDTO author = TestDataUtil.createTestAuthorDTO1();
        author.setAge(20);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);
        author.setAge(99);
        author.setName("UPDATED");
        String authorJson = objectMapper.writeValueAsString(authorMapper.mapTo(author));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateUpdatedExistingAuthor() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);
        author.setAge(99);
        author.setName("UPDATED");
        String authorJson = objectMapper.writeValueAsString(authorMapper.mapTo(author));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200ok() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);
        author.setAge(99);
        author.setName("UPDATED");
        String authorJson = objectMapper.writeValueAsString(authorMapper.mapTo(author));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);
        author.setAge(99);
        author.setName("UPDATED");
        String authorJson = objectMapper.writeValueAsString(authorMapper.mapTo(author));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204NoContentForExistingAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor1();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/authors/" + author.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204NoContentForNonExistingAuthor() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/authors/" + "99")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
