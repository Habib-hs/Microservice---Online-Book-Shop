package com.example.BookService.exception;

public class InventoryCreationException extends RuntimeException{
    public InventoryCreationException(String message) {
        super(message);
    }

    public InventoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
