package com.BjitMidTerm.Inventoryservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResModel {
    private Long inventoryId;
    private Integer quantity;
    private Double price;
}
