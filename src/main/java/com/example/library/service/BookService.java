package com.example.library.service;

import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Book;
import com.example.library.repo.BookRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepo bookRepo;

    @Autowired
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    public Book addBook(Book book) {
        return bookRepo.save(book);
    }

    public Book updateBook(Book book) {
        return bookRepo.save(book);
    }

    public Optional<Book> findBookById(Long id) {

        return Optional.ofNullable(bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book by id " + id + " was not found.")));
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }
}
