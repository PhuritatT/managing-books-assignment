package com.example.managing_books.service;

import com.example.managing_books.dtos.addBook;
import com.example.managing_books.dtos.apiResponseAddBook;
import org.springframework.http.ResponseEntity;

public interface BookManagementService {
    ResponseEntity<apiResponseAddBook> addBook(addBook addBook);
}
