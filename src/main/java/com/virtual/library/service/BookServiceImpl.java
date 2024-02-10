package com.virtual.library.service;

import com.virtual.library.model.Author;
import com.virtual.library.model.Book;
import com.virtual.library.repository.AuthorRepository;
import com.virtual.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book getBookInfo(int id) {
        return bookRepository.getBookById(id);
    }

    @Override
    public Book addNewBook(String title, LocalDate datePublication, String nameAuthor) {
        Book book = bookRepository.getBookByTitleAndAuthor(title, authorRepository.getByName(nameAuthor));
        if (book == null) {
            Book newBook = new Book(UUID.randomUUID(), title, datePublication);
            Author authorFromRepo = authorRepository.getByName(nameAuthor);
            if (authorFromRepo != null) {
                newBook.setAuthor(authorFromRepo);
            } else {
                Author newAuthor = new Author(UUID.randomUUID(), nameAuthor, new ArrayList<>());
                authorRepository.save(newAuthor);
                System.out.println(authorRepository.getByName(nameAuthor));
                newBook.setAuthor(newAuthor);
            }
            bookRepository.save(newBook);
            return newBook;
        } else {
            return book;
        }
    }

    @Override
    public void deleteBook(int id) {
        if (bookRepository.getBookById(id) != null) {
            bookRepository.deleteById(id);
        }
    }

    @Override
    public void updateBook(int id) {
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
