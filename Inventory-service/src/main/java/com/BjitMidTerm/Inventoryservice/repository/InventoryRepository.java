package com.BjitMidTerm.Inventoryservice.repository;

import com.BjitMidTerm.Inventoryservice.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {
    InventoryEntity findByInventoryId(long bookId);
}
