package com.example.managing_books.dtos;

public class apiResponseGet {
    private boolean success;
    private String message;
    private BookProjection[] books;

    public apiResponseGet (boolean success, String message, BookProjection[] books) {
        this.success = success;
        this.message = message;
        this.books = books;
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

    public BookProjection[] getBooks() {
        return books;
    }

    public void setBooks(BookProjection[] books) {
        this.books = books;
    }
}
