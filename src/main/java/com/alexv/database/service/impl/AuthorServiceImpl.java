package com.alexv.database.service.impl;

import com.alexv.database.domain.entity.AuthorEntity;
import com.alexv.database.repository.AuthorRepository;
import com.alexv.database.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {

        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        return savedAuthorEntity;
    }

    @Override
    public List<AuthorEntity> listAuthors() {

        List<AuthorEntity> authors =  StreamSupport.stream(authorRepository
                .findAll()
                .spliterator(),
                false)
                .toList();

        return authors;
    }

    @Override
    public Optional<AuthorEntity> getAuthor(Long id) {

        Optional<AuthorEntity> author = authorRepository.findById(id);

        return author;
    }

    @Override
    public Boolean isExists(Long id) {

        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {

        authorEntity.setId(id);

        return authorRepository.findById(id).map(
                existingAuthor -> {
                    Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
                    Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
                    return authorRepository.save(existingAuthor);
                }
        ).orElseThrow(() -> new RuntimeException("Author does not exist."));
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
