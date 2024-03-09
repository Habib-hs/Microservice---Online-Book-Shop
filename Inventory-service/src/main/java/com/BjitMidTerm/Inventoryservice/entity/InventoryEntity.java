package com.BjitMidTerm.Inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InventoryEntity {
    @Id
    private Long inventoryId;
    private Integer quantity;
    private Double price;
}
