package com.example.BookService.model;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseModel {
    private Long bookId;
    private String bookName;
    private String authorName;
    private String genre;
    private double price;
    private Integer quantity;
}
