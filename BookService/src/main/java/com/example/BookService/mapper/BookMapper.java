package com.example.BookService.mapper;

import com.example.BookService.entity.Book;
import com.example.BookService.model.BookModel;
import com.example.BookService.model.BookResponseModel;
import com.example.BookService.model.InventoryModel;

public class BookMapper {

    public static BookModel bookToBookModel(Book book){
        BookModel bookModel=new BookModel();
        bookModel.setBookId(book.getId());
        bookModel.setBookName(book.getBookName());
        bookModel.setAuthorName(book.getAuthorName());
        bookModel.setGenre(book.getGenre());

        return bookModel;
    }
    public static Book bookModelToBook(BookModel bookModel){
        Book book = new Book();
        book.setBookName(bookModel.getBookName());
        book.setAuthorName(bookModel.getAuthorName());
        book.setGenre(bookModel.getGenre());

        return book;
    }

    public static BookResponseModel bookToBookResponse(Book book, InventoryModel inventoryModel){
        BookResponseModel bookResponseModel = new BookResponseModel();

        bookResponseModel.setBookId(book.getId());
        bookResponseModel.setBookName(book.getBookName());
        bookResponseModel.setAuthorName(book.getAuthorName());
        bookResponseModel.setGenre(book.getGenre());

        bookResponseModel.setPrice(inventoryModel.getPrice());
        bookResponseModel.setQuantity(inventoryModel.getQuantity());

        return bookResponseModel;
    }
}
