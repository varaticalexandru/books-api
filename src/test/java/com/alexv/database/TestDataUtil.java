package com.alexv.database;

import com.alexv.database.domain.dto.AuthorDTO;
import com.alexv.database.domain.dto.BookDTO;
import com.alexv.database.domain.entity.AuthorEntity;
import com.alexv.database.domain.entity.BookEntity;

public final class TestDataUtil {

    private TestDataUtil() {}

    public static AuthorEntity createTestAuthor1() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Abigale Rose")
                .age(80)
                .build();
    }

    public static AuthorEntity createTestAuthor2() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Jordan Pickford")
                .age(22)
                .build();
    }

    public static AuthorEntity createTestAuthor3() {
        return AuthorEntity.builder()
                .id(3L)
                .name("John Smith")
                .age(44)
                .build();
    }

    public static AuthorDTO createTestAuthorDTO1() {
        return AuthorDTO.builder()
                .id(1L)
                .name("Abigale Rose")
                .age(80)
                .build();
    }


    public static BookEntity createTestBook1(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow in the Attic")
                .author(authorEntity)
                .build();
    }

    public static BookEntity createTestBook2(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-1")
                .title("A Tale of Two Cities")
                .author(authorEntity)
                .build();
    }

    public static BookEntity createTestBook3(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Lord of the Rings")
                .author(authorEntity)
                .build();
    }

    public static BookDTO createTestBookDTO1(final AuthorDTO authorDTO) {
        return BookDTO.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow in the Attic")
                .author(authorDTO)
                .build();
    }
}
