package com.virtual.library.service;

import com.virtual.library.model.Book;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.util.List;

public interface BookService {

    Book getBookInfo(int id);

    Book addNewBook(String title, String datePublication, String nameAuthor);

    void deleteBook(int id);

    Book updateBook(int id, String title, String date, String author);

    Page<Book> getAllBooks(int offset, int limit, String sort);

    List<Book> getListOfBooks();
}