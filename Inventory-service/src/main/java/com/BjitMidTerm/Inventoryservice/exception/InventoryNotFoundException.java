package com.BjitMidTerm.Inventoryservice.exception;

public class InventoryNotFoundException extends RuntimeException{
   public InventoryNotFoundException(String message){
       super(message);
    }
}
