package com.BjitMidTerm.Inventoryservice.service;

import com.BjitMidTerm.Inventoryservice.model.InventoryReqModel;
import com.BjitMidTerm.Inventoryservice.model.InventoryResModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {
    ResponseEntity<InventoryResModel> updateInventory(long bookId, InventoryReqModel inventoryDto);

    ResponseEntity<String> deleteInventory(Long bookId);

    ResponseEntity<InventoryResModel> getInventoryDetails(Long id);

    //    List<InventoryResModel>  getAllInventory(BookIdsModel ids);
    ResponseEntity<Object> getAllInventory(List<Long> ids);
}
