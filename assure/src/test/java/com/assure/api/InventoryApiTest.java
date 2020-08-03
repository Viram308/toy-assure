package com.assure.api;

import com.assure.pojo.Inventory;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InventoryApiTest extends AbstractUnitTest {
    private Inventory inventory1, inventory2;
    @Autowired
    private InventoryApi inventoryApi;


    @Before
    public void setUp(){
        inventory1 = createObject(1L, 20L, 2L, 5L);
        inventory2 = createObject(2L, 30L, 3L, 6L);
    }

    public Inventory createObject(Long globalSkuId, Long availableQty, Long allocatedQty, Long fulfilledQty) {
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(globalSkuId);
        inventory.setAvailableQuantity(availableQty);
        inventory.setAllocatedQuantity(allocatedQty);
        inventory.setFulfilledQuantity(fulfilledQty);
        return inventory;
    }

    @Test
    public void testAddInventory(){
        Inventory newInventory = inventoryApi.add(inventory1);
        assertNotNull(newInventory);
        assertTrue(newInventory.getId()>0);
        assertEquals(inventory1.getGlobalSkuId(), newInventory.getGlobalSkuId());
        assertEquals(inventory1.getAvailableQuantity(), newInventory.getAvailableQuantity());
        assertEquals(inventory1.getAllocatedQuantity(), newInventory.getAllocatedQuantity());
        assertEquals(inventory1.getFulfilledQuantity(), newInventory.getFulfilledQuantity());
    }

    @Test
    public void testGetInventoryDetails(){
        Inventory newInventory = inventoryApi.add(inventory1);
        Inventory result = inventoryApi.getInventoryByGlobalSkuId(newInventory.getGlobalSkuId());

        assertNotNull(result);
        assertEquals(newInventory.getGlobalSkuId(), result.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), result.getAvailableQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), result.getAllocatedQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), result.getFulfilledQuantity());
    }

    @Test
    public void testGetAllInventory(){
        inventoryApi.add(inventory1);
        inventoryApi.add(inventory2);

        List<Inventory> list = inventoryApi.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void testUpdateInventory(){
        inventoryApi.add(inventory1);
        Inventory newInventory = new Inventory();
        newInventory.setAvailableQuantity(40L);
        newInventory.setAllocatedQuantity(4L);
        newInventory.setFulfilledQuantity(10L);

        inventoryApi.update(inventory1.getId(),newInventory);
        Inventory updatedInventory = inventoryApi.getInventoryByGlobalSkuId(inventory1.getGlobalSkuId());
        assertNotNull(updatedInventory);
        assertEquals(newInventory.getAvailableQuantity(), updatedInventory.getAvailableQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), updatedInventory.getAllocatedQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), updatedInventory.getFulfilledQuantity());
    }

    @Test(expected = ApiException.class)
    public void testGetCheck(){
        Inventory newInventory = inventoryApi.add(inventory1);
        Inventory result = inventoryApi.getCheck(newInventory.getId());
        assertNotNull(result);
        assertEquals(newInventory.getGlobalSkuId(), result.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), result.getAvailableQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), result.getAllocatedQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), result.getFulfilledQuantity());
        // throw exception
        inventoryApi.getCheck(newInventory.getId()+1);
    }

    @Test
    public void testSearch(){
        Inventory inventory = inventoryApi.add(inventory1);
        List<Long> globalSkuIdList = new ArrayList<>();
        globalSkuIdList.add(inventory.getGlobalSkuId()+1);
        List<Inventory> inventoryList = inventoryApi.searchByGlobalSkuIdList(globalSkuIdList);
        assertEquals(0,inventoryList.size());
        globalSkuIdList.add(inventory.getGlobalSkuId());
        inventoryList = inventoryApi.searchByGlobalSkuIdList(globalSkuIdList);
        assertEquals(1,inventoryList.size());
    }

}
