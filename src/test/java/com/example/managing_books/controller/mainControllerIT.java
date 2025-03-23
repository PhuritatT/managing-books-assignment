package com.example.managing_books.controller;

import com.example.managing_books.dtos.BookProjection;
import com.example.managing_books.dtos.addBook;
import com.example.managing_books.dtos.apiResponseAddBook;
import com.example.managing_books.dtos.apiResponseGet;
import com.example.managing_books.service.BookManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class mainControllerIT {

    private final MockMvc mockMvc;

    @Autowired
    public mainControllerIT(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void TEST_ADD_BOOK_VALIDATION_ERROR() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Book\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Title and Author are required"));
    }

    @Test
    public void TEST_ADD_BOOK_1000s() throws Exception {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Test Book");
        newBook.setAuthor("Test Author");
        newBook.setPublishedDate(Date.valueOf("1000-01-01"));

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBook.addBookString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book cannot be published before 1000s"));
    }

    @Test
    public void TEST_ADD_BOOK_SUCCESS_GREGORIAN_DATE() throws Exception {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Test Book");
        newBook.setAuthor("Test Author");
        newBook.setPublishedDate(Date.valueOf("2022-01-01"));

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBook.addBookString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book added successfully"));
    }

    @Test
    public void TEST_ADD_BOOK_SUCCESS_BUDDHIST_DATE() throws Exception {
        // Arrange
        addBook bookWithFutureDate = new addBook();
        bookWithFutureDate.setTitle("Test Book");
        bookWithFutureDate.setAuthor("Test Author");
        bookWithFutureDate.setPublishedDate(Date.valueOf("2560-01-01"));


        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookWithFutureDate.addBookString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book added successfully"));
    }

    @Test
    public void TEST_ADD_BOOK_FUTURE_GREGORIAN_DATE() throws Exception {
        // Arrange
        addBook bookWithFutureDate = new addBook();
        bookWithFutureDate.setTitle("Test Book");
        bookWithFutureDate.setAuthor("Test Author");
        bookWithFutureDate.setPublishedDate(Date.valueOf("2099-01-01"));


        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookWithFutureDate.addBookString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book cannot be published in the future"));
    }

    @Test
    public void TEST_ADD_BOOK_FUTURE_BUDDHIST_DATE() throws Exception {
        // Arrange
        addBook bookWithFutureDate = new addBook();
        bookWithFutureDate.setTitle("Test Book");
        bookWithFutureDate.setAuthor("Test Author");
        bookWithFutureDate.setPublishedDate(Date.valueOf("2570-01-01"));


        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookWithFutureDate.addBookString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book cannot be published in the future"));
    }

    @Test
    public void TEST_GET_BOOK_AUTHOR_FOUND() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/books").param("author", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.books").isArray());
    }

    @Test
    public void TEST_GET_BOOK_AUTHOR_NOT_FOUND() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/books").param("author", "Unknown Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(0));
    }
}