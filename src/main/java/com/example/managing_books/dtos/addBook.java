package com.example.managing_books.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class addBook {

    @NotBlank(message = "Author is required")
    @NotNull(message = "Title cannot be null")
    @Schema(description = "Title of the book", example = "The Great Gatsby")
    private String title;

    @NotBlank(message = "Author is required")
    @NotNull(message = "Author cannot be null")
    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald")
    private String author;

    @Schema(description = "Published date of the book", example = "1925-04-10")
    private Date publishedDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}