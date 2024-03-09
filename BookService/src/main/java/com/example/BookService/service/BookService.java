package com.example.BookService.service;

import com.example.BookService.model.BookBuyModel;
import com.example.BookService.model.BookModel;
import com.example.BookService.model.BookResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {
    ResponseEntity<String> createBook(BookModel bookModel);

    List<BookResponseModel> getAllBook();
    BookResponseModel getBookById(Long bookId);

    BookModel updateBook(BookModel bookModel);

    ResponseEntity<String> deleteBook(Long bookId);
    ResponseEntity<String> buyBook(BookBuyModel bookBuyModel);
}
