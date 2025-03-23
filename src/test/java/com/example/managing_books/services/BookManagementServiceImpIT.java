package com.example.managing_books.services;

import com.example.managing_books.dtos.BookProjection;
import com.example.managing_books.dtos.addBook;
import com.example.managing_books.dtos.apiResponseAddBook;
import com.example.managing_books.dtos.apiResponseGet;
import com.example.managing_books.entity.tbBooks;
import com.example.managing_books.repositories.TbBooksRepositories;
import com.example.managing_books.service.imp.BookManagmentServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Transactional
public class BookManagementServiceImpIT {

    private final BookManagmentServiceImp underTest;
    private final TbBooksRepositories tbBooksRepositories;

    @Autowired
    public BookManagementServiceImpIT(BookManagmentServiceImp underTest,
                                      TbBooksRepositories tbBooksRepositories) {
        this.underTest = underTest;
        this.tbBooksRepositories = tbBooksRepositories;
    }

    @BeforeEach
    public void setup() {
        // Clean up the database before each test
        tbBooksRepositories.deleteAll();

        // Add test data
        tbBooks book1 = new tbBooks();
        book1.setTitle("Test Book 1");
        book1.setAuthor("Test Author");
        book1.setPublishedDate(Date.valueOf("2020-01-01"));
        book1.setCreatedAt(new Date(System.currentTimeMillis()));

        tbBooks book2 = new tbBooks();
        book2.setTitle("Test Book 2");
        book2.setAuthor("Test Author");
        book2.setPublishedDate(Date.valueOf("2021-05-15"));
        book2.setCreatedAt(new Date(System.currentTimeMillis()));

        tbBooksRepositories.saveAll(Arrays.asList(book1, book2));
    }

    @Test
    public void TEST_ADD_BOOK_SUCCESS_GREGORIAN_DATE() {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Integration Test Book");
        newBook.setAuthor("Integration Test Author");
        newBook.setPublishedDate(Date.valueOf("2022-03-15"));

        // Act
        ResponseEntity<apiResponseAddBook> response = underTest.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Book added successfully", response.getBody().getMessage());

        // Verify book was added to database
        List<BookProjection> books = tbBooksRepositories.findByAuthor("Integration Test Author");
        assertEquals(1, books.size());
        assertEquals("Integration Test Book", books.get(0).getTitle());
    }

    @Test
    public void TEST_ADD_BOOK_SUCCESS_BUDDHIST_DATE() {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Integration Test Book");
        newBook.setAuthor("Integration Test Author");
        newBook.setPublishedDate(Date.valueOf("2539-03-15"));

        // Act
        ResponseEntity<apiResponseAddBook> response = underTest.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Book added successfully", response.getBody().getMessage());

        // Verify book was added to database
        List<BookProjection> books = tbBooksRepositories.findByAuthor("Integration Test Author");
        assertEquals(1, books.size());
        assertEquals("Integration Test Book", books.get(0).getTitle());
    }

    @Test
    public void TEST_ADD_BOOK_FUTURE_GREGORIAN_DATE() {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Integration Test Book");
        newBook.setAuthor("Integration Test Author");
        newBook.setPublishedDate(Date.valueOf("2099-12-31")); // Future date

        // Act
        ResponseEntity<apiResponseAddBook> response = underTest.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Book cannot be published in the future", response.getBody().getMessage());
    }

    @Test
    public void TEST_ADD_BOOK_FUTURE_BUDDHIST_DATE() {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Integration Test Book");
        newBook.setAuthor("Integration Test Author");
        newBook.setPublishedDate(Date.valueOf("2580-12-31")); // Future date

        // Act
        ResponseEntity<apiResponseAddBook> response = underTest.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Book cannot be published in the future", response.getBody().getMessage());
    }

    @Test
    public void TEST_ADD_BOOK_1000s() {
        // Arrange
        addBook newBook = new addBook();
        newBook.setTitle("Integration Test Book");
        newBook.setAuthor("Integration Test Author");
        newBook.setPublishedDate(Date.valueOf("1000-03-15"));

        // Act
        ResponseEntity<apiResponseAddBook> response = underTest.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Book cannot be published before 1000s", response.getBody().getMessage());
    }

    @Test
    public void TEST_ADD_BOOK_MISSING_REQUIRED_FIELD() {
        // Arrange
        addBook newBook = new addBook();
        newBook.setAuthor("Integration Test Author");
        // Title is missing

        // Act
        ResponseEntity<apiResponseAddBook> response = underTest.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Title and Author are required", response.getBody().getMessage());
    }

    @Test
    public void TEST_GET_BOOK_BY_AUTHOR_FOUND() {
        // Act
        ResponseEntity<apiResponseGet> response = underTest.getBookByAuthor("Test Author");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Books retrieved successfully", response.getBody().getMessage());

        BookProjection[] books = response.getBody().getBooks();
        assertEquals(2, books.length);

        // Verify book titles
        boolean foundBook1 = false;
        boolean foundBook2 = false;

        for (BookProjection book : books) {
            if ("Test Book 1".equals(book.getTitle())) foundBook1 = true;
            if ("Test Book 2".equals(book.getTitle())) foundBook2 = true;
        }

        assertTrue(foundBook1 && foundBook2, "Both test books should be found");
    }

    @Test
    public void TEST_GET_BOOK_BY_AUTHOR_NOT_FOUND() {
        // Act
        ResponseEntity<apiResponseGet> response = underTest.getBookByAuthor("Non-existent Author");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Books retrieved successfully", response.getBody().getMessage());
        assertEquals(0, response.getBody().getBooks().length, "Should return empty array for non-existent author");
    }
}