package com.BjitMidTerm.Inventoryservice.service.Imp;

import com.BjitMidTerm.Inventoryservice.entity.InventoryEntity;
import com.BjitMidTerm.Inventoryservice.exception.InventoryNotFoundException;
import com.BjitMidTerm.Inventoryservice.mapper.InventoryMapperModel;
import com.BjitMidTerm.Inventoryservice.model.InventoryReqModel;
import com.BjitMidTerm.Inventoryservice.model.InventoryResModel;
import com.BjitMidTerm.Inventoryservice.repository.InventoryRepository;
import com.BjitMidTerm.Inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImp implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    public ResponseEntity<InventoryResModel> updateInventory(long bookId,InventoryReqModel inventoryReqModel){
        InventoryEntity existInventory=inventoryRepository.findByInventoryId(bookId);// check inventory, are already exist or not using bookId.
        InventoryEntity updateInventory = inventoryRepository.save(InventoryMapperModel.inventoryReqModelToEntity(inventoryReqModel));//update the inventory and save it database
        InventoryResModel responseInventory= InventoryMapperModel.inventoryEntityToResModel(updateInventory); //convert the update entity model to response model
        if (existInventory!=null){
            return new ResponseEntity<>(responseInventory,HttpStatus.CREATED);// if bookId is not available then it give a created status.
        }
        return new ResponseEntity<>(responseInventory,HttpStatus.OK);// if bookId is  available then it give an ok status.
    }

    @Override
    public ResponseEntity<String> deleteInventory(Long bookId) {
        InventoryEntity inventory=inventoryRepository.findByInventoryId(bookId);// check inventory, are already exist or not using bookId.
        if (inventory!=null){
            inventoryRepository.delete(inventory);
            return new ResponseEntity<>("SuccessFull Deleted the Inventory",HttpStatus.OK);// if bookId is  available then it is deleted and  give an ok status.
        }
        throw new InventoryNotFoundException("Please enter a valid Id then try to delete");
    }

    @Override
    public ResponseEntity<InventoryResModel> getInventoryDetails(Long id) {
        InventoryEntity inventory=inventoryRepository.findByInventoryId(id);// check inventory, are already exist or not using bookId.
        if (inventory!=null){
            return new ResponseEntity<>(InventoryMapperModel.inventoryEntityToResModel(inventory),HttpStatus.OK);
        }
        throw new InventoryNotFoundException("Please enter a valid Id then try to fetch the details");
    }
    @Override
    public  ResponseEntity<Object> getAllInventory(List<Long> ids) {
        int inventoryListLength=inventoryRepository.findAll().size();// length of the inventory.
        if(inventoryListLength!=ids.size()){//checks ids length and inventory length is equal or not
            throw new  InventoryNotFoundException("book Service length and inventory service is not same");
        }
        List<InventoryResModel> inventoryLists=new ArrayList<>(); //create a InventoryResModel type list for output
        for (Long id:ids){
            InventoryEntity inventoryEntity=inventoryRepository.findByInventoryId(id);//find inventory using inventory id
            inventoryLists.add(InventoryMapperModel.inventoryEntityToResModel(inventoryEntity)); // add the inventoryEntity to inventoryLists
        }
        return new ResponseEntity<>(inventoryLists,HttpStatus.OK);
    }
}
