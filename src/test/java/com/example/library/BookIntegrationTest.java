package com.example.library;

import com.example.library.model.Book;
import com.example.library.repo.BookRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepo.deleteAll();
    }

    @Test
    void whenAddBook_thenBookIsCreated() throws Exception {
        Book book = new Book("Integration Test Book", "Test Author", "Test Description");

        mockMvc.perform(post("/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.description").value(book.getDescription()));
    }

    @Test
    void whenGetAllBooks_thenReturnJsonArray() throws Exception {
        Book book1 = new Book("Book 1", "Author 1", "Description 1");
        Book book2 = new Book("Book 2", "Author 2", "Description 2");

        bookRepo.save(book1);
        bookRepo.save(book2);

        mockMvc.perform(get("/book/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(book1.getName()))
                .andExpect(jsonPath("$[1].name").value(book2.getName()));
    }

    @Test
    void whenGetBookById_thenReturnBook() throws Exception {
        Book book = bookRepo.save(new Book("Test Book", "Test Author", "Test Description"));

        mockMvc.perform(get("/book/find/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()));
    }

    @Test
    void whenDeleteBook_thenStatusOk() throws Exception {
        Book book = bookRepo.save(new Book("Test Book", "Test Author", "Test Description"));

        mockMvc.perform(delete("/book/delete/" + book.getId()))
                .andExpect(status().isOk());
    }
}
