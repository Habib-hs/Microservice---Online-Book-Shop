package com.BjitMidTerm.Inventoryservice.mapper;

import com.BjitMidTerm.Inventoryservice.entity.InventoryEntity;
import com.BjitMidTerm.Inventoryservice.model.InventoryReqModel;
import com.BjitMidTerm.Inventoryservice.model.InventoryResModel;

public class InventoryMapperModel {
    public  static InventoryEntity inventoryReqModelToEntity(InventoryReqModel inventoryReqModel){
        return InventoryEntity.builder()
                .inventoryId(inventoryReqModel.getInventoryId())
                .price(inventoryReqModel.getPrice())
                .quantity(inventoryReqModel.getQuantity())
                .build();
    }
    public  static InventoryResModel inventoryEntityToResModel(InventoryEntity inventoryEntity){
        return InventoryResModel.builder()
                .inventoryId(inventoryEntity.getInventoryId())
                .price(inventoryEntity.getPrice())
                .quantity(inventoryEntity.getQuantity())
                .build();
    }
}
