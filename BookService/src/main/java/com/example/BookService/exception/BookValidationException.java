package com.example.BookService.exception;

public class BookValidationException extends RuntimeException{
    public BookValidationException(String message){
        super(message);
    }
}
