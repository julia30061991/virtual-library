package com.virtual.library.repository;

import com.virtual.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Integer> {

    Author getAuthorById(int id);

    @Override
    List<Author> findAll();

    Author getAuthorByName(String name);
}
