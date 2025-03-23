package com.example.managing_books.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class apiResponseAddBook {

    @Schema(description = "Success status of the request", example = "true")
    private boolean success;

    @Schema(description = "Message describing the result of the request", example = "Book added successfully")
    private String message;

    public apiResponseAddBook(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
