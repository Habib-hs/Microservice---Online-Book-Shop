package com.example.BookService.exception;

public class InventoryDeletionException extends RuntimeException{
    public InventoryDeletionException(String message){
        super(message);
    }
}
