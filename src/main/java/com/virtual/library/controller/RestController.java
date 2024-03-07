package com.virtual.library.controller;

import com.virtual.library.model.Book;
import com.virtual.library.service.BookServiceImpl;
import com.virtual.library.service.ExcelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private final BookServiceImpl bookService;

    public RestController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookInfo(@PathVariable("id") int id) {
        Book book = bookService.getBookInfo(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/book/add")
    public ResponseEntity<Book> addNewBook(@RequestParam String title, String date, String nameAuthor) {
        Book book = bookService.addNewBook(title, date, nameAuthor);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/delete/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/book/update/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable("id") int id,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) String date,
                                             @RequestParam(required = false) String author) {
        Book book = bookService.updateBook(id, title, date, author);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Книга в бибилиотеке отсутствует", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/books")
    public Page<Book> getAllBooks(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                  @RequestParam(value = "limit", defaultValue = "10") int limit,
                                  @RequestParam(value = "sort") String sortField) {
        return bookService.getAllBooks(offset, limit, sortField);
    }


    @GetMapping("/export")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Book> listOfBooks = bookService.getListOfBooks();
        ExcelGenerator generator = new ExcelGenerator();
        generator.generateExcelFile(response, listOfBooks);
    }
}