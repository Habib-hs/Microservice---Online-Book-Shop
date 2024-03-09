package com.example.BookService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryModel {
    private Long inventoryId;
    private double price;
    private Integer quantity;
}
