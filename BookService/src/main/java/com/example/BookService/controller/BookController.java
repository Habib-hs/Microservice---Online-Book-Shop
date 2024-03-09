package com.example.BookService.controller;

import com.example.BookService.exception.BookValidationException;
import com.example.BookService.model.BookBuyModel;
import com.example.BookService.model.BookModel;
import com.example.BookService.model.BookResponseModel;
import com.example.BookService.service.BookService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/book-service")
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<String> createBook(@RequestBody BookModel bookModel) {
        return bookService.createBook(bookModel);
    }


    @GetMapping("/book/all")
    public ResponseEntity<List<BookResponseModel>> getAllBook() {
        List<BookResponseModel> bookResponseModel = bookService.getAllBook();
        return ResponseEntity.ok(bookResponseModel);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseModel> getBook(@PathVariable("id") Long bookId) {
        BookResponseModel bookResponseModel = bookService.getBookById(bookId);
        return ResponseEntity.ok(bookResponseModel);
    }

    @PutMapping("/update")
    public ResponseEntity<BookModel> updateBook(@RequestBody BookModel bookModel) {
        bookService.updateBook(bookModel);

        return ResponseEntity.ok(bookModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long bookId) {
        return bookService.deleteBook(bookId);
    }

    @PostMapping("/book/buy")
    public ResponseEntity<String> buyBook(@RequestBody BookBuyModel bookBuyModel){
        return bookService.buyBook(bookBuyModel);
    }


}
