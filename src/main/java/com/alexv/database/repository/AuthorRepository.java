package com.alexv.database.repository;

import com.alexv.database.domain.entity.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {

    List<AuthorEntity> ageLessThan(int i);

    @Query(value = "SELECT a from AuthorEntity a where a.age > ?1")
    List<AuthorEntity> authorsWithAgeGreaterThan(int i);
}
