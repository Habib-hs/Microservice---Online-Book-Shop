package com.example.BookService.exception;

public class BookCreationException extends RuntimeException{
    public BookCreationException(String message) {
        super(message);
    }

    public BookCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
