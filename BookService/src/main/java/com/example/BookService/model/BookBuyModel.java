package com.example.BookService.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookBuyModel {
    private Long buyId;
    private Integer quantity;
}
