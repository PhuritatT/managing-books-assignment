package com.example.managing_books.controller;

import com.example.managing_books.dtos.addBook;
import com.example.managing_books.dtos.apiResponseAddBook;
import com.example.managing_books.dtos.apiResponseGet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.managing_books.service.BookManagementService;

@RestController
public class mainController {

    @Autowired
    private BookManagementService bookManagementService;

    @Tag(name = "Add Books")
    @Operation(
            summary = "Add a new book",
            description = "Creates a new book record in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = apiResponseAddBook.class)
                            )
                    )
            }
    )
    @PostMapping("/books")
    public ResponseEntity<apiResponseAddBook> addBook(@Valid @RequestBody addBook book) {
        return bookManagementService.addBook(book);
    }

    @Tag(name = "Get Books")
    @Operation(
            summary = "Get books by author",
            description = "Retrieves books written by the specified author",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved books",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = apiResponseGet.class)
                            )
                    )
            }
    )
    @GetMapping("/books")
    public ResponseEntity<apiResponseGet> getBookByAuthor(@RequestParam String author) {
        return bookManagementService.getBookByAuthor(author);
    }

}
