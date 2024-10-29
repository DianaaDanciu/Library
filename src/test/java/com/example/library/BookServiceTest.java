package com.example.library;

import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Book;
import com.example.library.repo.BookRepo;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "Test Author", "Test Description");
        testBook.setId(1L);
    }

    @Test
    void findAllBooks_ShouldReturnListOfBooks() {
        List<Book> expectedBooks = Arrays.asList(
                testBook,
                new Book("Book 2", "Author 2", "Description 2")
        );
        when(bookRepo.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findAllBooks();

        assertThat(actualBooks).hasSize(2);
        assertThat(actualBooks).isEqualTo(expectedBooks);
        verify(bookRepo, times(1)).findAll();
    }


    @Test
    void addBook_ShouldReturnSavedBook() {
        when(bookRepo.save(any(Book.class))).thenReturn(testBook);

        Book savedBook = bookService.addBook(testBook);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getName()).isEqualTo("Test Book");
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    @Test
    void findBookById_WhenBookExists_ShouldReturnBook() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(testBook));

        Optional<Book> found = bookService.findBookById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void findBookById_WhenBookDoesNotExist_ShouldThrowException() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.findBookById(1L);
        });
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void deleteBook_ShouldCallRepository() {
        bookService.deleteBook(1L);

        verify(bookRepo, times(1)).deleteById(1L);
    }
}
