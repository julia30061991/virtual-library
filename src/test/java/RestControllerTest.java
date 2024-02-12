import com.virtual.library.controller.RestController;
import com.virtual.library.model.Book;
import com.virtual.library.service.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RestControllerTest {

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private RestController restController;

    @Test
    public void returnBookInfo() {
        Book book = new Book();
        book.setId(1);
        book.setUuid(UUID.randomUUID());
        book.setTitle("А зори здесь тихие");
        book.setDataPublication(LocalDate.now());

        Mockito.when(bookService.getBookInfo(1)).thenReturn(book);
        ResponseEntity<Book> response = restController.getBookInfo(1);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(new ResponseEntity<>(book, HttpStatus.OK), restController.getBookInfo(1));
    }

    @Test
    public void addNewBook() {
        Book book = new Book();
        book.setId(2);
        book.setUuid(UUID.randomUUID());
        book.setTitle("Ночь перед Рождеством");
        book.setDataPublication(LocalDate.now());

        Mockito.when(bookService.addNewBook("Ночь перед Рождеством", LocalDate.now(),
                "Н.В.Гоголь")).thenReturn(book);
        ResponseEntity<Book> response = restController.addNewBook("Ночь перед Рождеством", LocalDate.now(),
                "Н.В.Гоголь");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(new ResponseEntity<>(book, HttpStatus.CREATED),
                restController.addNewBook("Ночь перед Рождеством", LocalDate.now(), "Н.В.Гоголь"));
    }

    @Test
    public void deleteBook() {
        Book book = new Book();
        book.setId(3);

        ResponseEntity<Book> response = restController.deleteBook(3);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateBook() {
        Book book1 = new Book();
        UUID uuid = UUID.randomUUID();
        String newTitle = "Первая книга стала второй";
        book1.setId(1);
        book1.setUuid(uuid);
        book1.setTitle("Первая книга");
        book1.setDataPublication(LocalDate.now());

        Mockito.when(bookService.updateBook(1, newTitle, LocalDate.now(),
                "Н.В.Гоголь")).thenReturn(new Book(1, uuid,
                newTitle, LocalDate.now(), null));
        ResponseEntity<Object> response = restController.updateBook(1, newTitle, LocalDate.now(),
                "Н.В.Гоголь");

        book1.setTitle("Первая книга стала второй");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(new ResponseEntity<>(book1, HttpStatus.OK), restController.updateBook(1,
                newTitle, LocalDate.now(), "Н.В.Гоголь"));
    }
}