package com.example.BookService.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookModel {
    private Long bookId;
    private String bookName;
    private String authorName;
    private String genre;
    private double price;
    private Integer quantity;
}
