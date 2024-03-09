package com.BjitMidTerm.Inventoryservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReqModel {
    private Long inventoryId;
    private Integer quantity;
    private Double price;
}
