package com.virtual.library.service;

import com.virtual.library.model.Book;
import java.time.LocalDate;
import java.util.List;

public interface BookService {

    Book getBookInfo(int id);

    Book addNewBook(String title, LocalDate datePublication, String nameAuthor);

    void deleteBook(int id);

    void updateBook(int id);

    List<Book> getAllBooks();

}
