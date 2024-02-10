package com.virtual.library.repository;

import com.virtual.library.model.Author;
import com.virtual.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository <Book, Integer> {

    Book getBookByTitleAndAuthor(String title, Author author);

    Book getBookById(int id);

    @Override
    List<Book> findAll();
}
