package com.assure.api;

import com.assure.dao.InventoryDao;
import com.assure.pojo.Inventory;
import com.commons.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional
    public Inventory add(Inventory inventory) {
        return inventoryDao.insert(inventory);
    }

    @Transactional(readOnly = true)
    public List<Inventory> getAll(){
        return inventoryDao.selectAll();
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryByGlobalSkuId(Long globalSkuId) {
        return inventoryDao.selectByGlobalSkuId(globalSkuId);
    }

    @Transactional
    public Inventory update(Long id,Inventory inventory){
        Inventory inventoryUpdate = getCheck(id);
        inventoryUpdate.setAvailableQuantity(inventory.getAvailableQuantity());
        inventoryUpdate.setAllocatedQuantity(inventory.getAllocatedQuantity());
        inventoryUpdate.setFulfilledQuantity(inventory.getFulfilledQuantity());
        return inventoryDao.update(inventoryUpdate);
    }

    @Transactional(readOnly = true)
    public Inventory getCheck(Long id) {
        Inventory inventory = inventoryDao.select(Inventory.class,id);
        if(inventory == null){
            throw new ApiException("Inventory doesn't exist for id : "+id);
        }
        return inventory;
    }

    public List<Inventory> searchByGlobalSkuIdList(List<Long> globalSkuIdList) {
        return inventoryDao.searchByGlobalSkuIdList(globalSkuIdList);
    }
}
