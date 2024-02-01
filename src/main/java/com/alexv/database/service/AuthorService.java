package com.alexv.database.service;

import com.alexv.database.domain.entity.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity saveAuthor(AuthorEntity authorEntity);
    List<AuthorEntity> listAuthors();
    Optional<AuthorEntity> getAuthor(Long id);
    Boolean isExists (Long id);
    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);
    void deleteAuthor(Long id);
}
