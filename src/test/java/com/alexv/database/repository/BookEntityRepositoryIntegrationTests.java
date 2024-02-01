package com.alexv.database.repository;

import com.alexv.database.TestDataUtil;
import com.alexv.database.domain.entity.AuthorEntity;
import com.alexv.database.domain.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private final BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthor1();

        BookEntity testBookEntity = TestDataUtil.createTestBook1(testAuthorEntity);
        underTest.save(testBookEntity);

        Optional<BookEntity> result = underTest.findById(testBookEntity.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testBookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthor1();

        BookEntity testBook1Entity = TestDataUtil.createTestBook1(testAuthorEntity);
        BookEntity testBook2Entity = TestDataUtil.createTestBook2(testAuthorEntity);
        BookEntity testBook3Entity = TestDataUtil.createTestBook3(testAuthorEntity);
        underTest.saveAll(List.of(testBook1Entity, testBook2Entity, testBook3Entity));

        Iterable<BookEntity> result = underTest.findAll();

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly(testBook1Entity, testBook2Entity, testBook3Entity);

    }

    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthor1();
        BookEntity testBookEntity = TestDataUtil.createTestBook1(testAuthorEntity);
        underTest.save(testBookEntity);

        testBookEntity.setTitle("A Fairy Tale");
        underTest.save(testBookEntity);

        Optional<BookEntity> result = underTest.findById("978-1-2345-6789-0");

        assertThat(result).isPresent();
        assertThat(result.get())
                .isEqualTo(testBookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthor1();
        BookEntity testBookEntity = TestDataUtil.createTestBook1(testAuthorEntity);
        underTest.save(testBookEntity);

        underTest.delete(testBookEntity);

        Optional<BookEntity> result = underTest.findById(testBookEntity.getIsbn());

        assertThat(result).isEmpty();
    }
}
