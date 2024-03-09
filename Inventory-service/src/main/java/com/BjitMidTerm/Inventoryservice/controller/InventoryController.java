package com.BjitMidTerm.Inventoryservice.controller;

import com.BjitMidTerm.Inventoryservice.model.InventoryReqModel;
import com.BjitMidTerm.Inventoryservice.model.InventoryResModel;
import com.BjitMidTerm.Inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<Object> getAllInventory(@RequestParam List<Long> ids) {
        return inventoryService.getAllInventory(ids);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<InventoryResModel> updateInventory(@PathVariable("bookId") Long bookId, @RequestBody InventoryReqModel inventoryReqModel) {
        return inventoryService.updateInventory(bookId, inventoryReqModel);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteInventory(@PathVariable("bookId") Long bookId) {
        return inventoryService.deleteInventory(bookId);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<InventoryResModel> getInventoryDetails(@PathVariable("bookId") Long id) {
        return inventoryService.getInventoryDetails(id);
    }

}
