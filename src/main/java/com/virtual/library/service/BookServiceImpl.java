package com.virtual.library.service;

import com.virtual.library.model.Author;
import com.virtual.library.model.Book;
import com.virtual.library.repository.AuthorRepository;
import com.virtual.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public Book addNewBook(String title, String datePublication, String nameAuthor) {
        Book book = bookRepository.getBookByTitleAndAuthor(title, authorRepository.getAuthorByName(nameAuthor));
        if (book == null) {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.parse(datePublication, pattern);
            Book newBook = new Book(UUID.randomUUID(), title, date);
            Author authorFromRepo = authorRepository.getAuthorByName(nameAuthor);
            if (authorFromRepo != null) {
                newBook.setAuthor(authorFromRepo);
            } else {
                Author newAuthor = new Author(UUID.randomUUID(), nameAuthor, new ArrayList<>());
                authorRepository.save(newAuthor);
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
    public Book updateBook(int id, String title, String date, String authorName) {
        Book book = bookRepository.getBookById(id);
        if (book != null) {
            //проверка названия книги
            if (title == null || title.isEmpty()) {
               book.setTitle(book.getTitle());
            } else book.setTitle(title);
            //проверка даты публикации книги
            if (date == null) {
               book.setDataPublication(book.getDataPublication());
            } else {
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate newDate = LocalDate.parse(date, pattern);
                book.setDataPublication(newDate);
            }
            //проверка автора книги
            if (authorName == null || authorName.isEmpty()) {
                book.setAuthor(book.getAuthor());
            } else {
                Author author = authorRepository.getAuthorByName(authorName);
                if (author != null) {
                    book.setAuthor(author);
                } else {
                    Author newAuthor = new Author(UUID.randomUUID(), authorName, new ArrayList<>());
                    book.setAuthor(newAuthor);
                }
            }
        } else {
            return null;
        }
        bookRepository.save(book);
        return book;
    }

    @Override
    public Page <Book> getAllBooks(int offset, int limit, String sortField) {
        //сортировка по полю сущности, передается в параметрах реквеста
        return bookRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField)));
    }

    @Override
    public List<Book> getListOfBooks() {
        return bookRepository.findAll();
    }
}
