package com.alexv.database.controller;

import com.alexv.database.domain.dto.AuthorDTO;
import com.alexv.database.domain.entity.AuthorEntity;
import com.alexv.database.mapper.Mapper;
import com.alexv.database.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDTO> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDTO> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }


    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO author) {

        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);

        AuthorDTO savedAuthorDTO = authorMapper.mapTo(savedAuthorEntity);

        return new ResponseEntity<>(savedAuthorDTO, HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public ResponseEntity<List<AuthorDTO>> listAuthors() {

        List<AuthorDTO> authors = authorService.listAuthors()
                .stream()
                .map(authorMapper::mapTo)
                .collect(Collectors.toList());

        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable("id") Long id) {

        if (!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AuthorDTO author = authorMapper.mapTo(authorService.getAuthor(id).get());

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> fullUpdateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDTO author) {

        if (!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        author.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthor = authorService.saveAuthor(authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(savedAuthor), HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorEntity> partialUpdate (
            @PathVariable("id") Long id,
            @RequestBody AuthorDTO author) {

        if (!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AuthorEntity updatedAuthor = authorService.partialUpdate(id, authorMapper.mapFrom(author));

        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id) {

        authorService.deleteAuthor(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
