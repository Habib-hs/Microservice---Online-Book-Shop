package com.example.BookService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BookAlreadyExistsException extends RuntimeException{
    public BookAlreadyExistsException(String message){
        super(message);
    }
}
