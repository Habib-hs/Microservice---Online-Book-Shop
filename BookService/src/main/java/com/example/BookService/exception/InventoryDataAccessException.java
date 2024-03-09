package com.example.BookService.exception;

public class InventoryDataAccessException extends RuntimeException{
    public InventoryDataAccessException(String message){
        super(message);
    }
}
