package com.alexv.database.repository;

import com.alexv.database.TestDataUtil;
import com.alexv.database.domain.entity.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthor1();

        underTest.save(testAuthorEntity);
        Optional<AuthorEntity> result = underTest.findById(testAuthorEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testAuthorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity testAuthor1Entity = TestDataUtil.createTestAuthor1();
        AuthorEntity testAuthor2Entity = TestDataUtil.createTestAuthor2();
        AuthorEntity testAuthor3Entity = TestDataUtil.createTestAuthor3();

        underTest.saveAll(List.of(testAuthor1Entity, testAuthor2Entity, testAuthor3Entity));

        Iterable<AuthorEntity> result = underTest.findAll();

        assertThat(result).isNotNull();
        assertThat(result)
                .hasSize(3)
                .containsExactly(testAuthor1Entity, testAuthor2Entity, testAuthor3Entity);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity testAuthor1Entity = TestDataUtil.createTestAuthor1();
        underTest.save(testAuthor1Entity);

        testAuthor1Entity.setName("UPDATED");

        underTest.save(testAuthor1Entity);

        Optional<AuthorEntity> result = underTest.findById(testAuthor1Entity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testAuthor1Entity);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthor1();
        underTest.save(testAuthorEntity);

        underTest.delete(testAuthorEntity);

        Optional<AuthorEntity> result = underTest.findById(testAuthorEntity.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatAuthorWithAgeLessThan() {
        AuthorEntity testAuthor1Entity = TestDataUtil.createTestAuthor1();
        AuthorEntity testAuthor2Entity = TestDataUtil.createTestAuthor2();
        AuthorEntity testAuthor3Entity = TestDataUtil.createTestAuthor3();

        underTest.saveAll(List.of(testAuthor1Entity, testAuthor2Entity, testAuthor3Entity));

        List<AuthorEntity> result = underTest.ageLessThan(50);

        assertThat(result)
                .isNotEmpty()
                .containsExactly(testAuthor2Entity, testAuthor3Entity);
    }

    @Test
    public void testThatAuthorWithAgeGreaterThan() {
        AuthorEntity testAuthor1Entity = TestDataUtil.createTestAuthor1();
        AuthorEntity testAuthor2Entity = TestDataUtil.createTestAuthor2();
        AuthorEntity testAuthor3Entity = TestDataUtil.createTestAuthor3();

        underTest.saveAll(List.of(testAuthor1Entity, testAuthor2Entity, testAuthor3Entity));

        List<AuthorEntity> result = underTest.authorsWithAgeGreaterThan(50);

        assertThat(result)
                .isNotEmpty()
                .containsExactly(testAuthor1Entity);
    }
}
