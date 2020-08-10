package com.assure.api;

import com.assure.pojo.BinSku;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BinSkuApiTest extends AbstractUnitTest {
    private BinSku newBinSku, binSku;
    @Autowired
    private BinSkuApi binSkuApi;

    @Before
    public void setUp() {
        binSku = createObject(1000L, 5L, 15L);
    }

    public BinSku createObject(Long binId, Long globalSkuId, Long quantity) {
        BinSku binSku = new BinSku();
        binSku.setBinId(binId);
        binSku.setQuantity(quantity);
        binSku.setGlobalSkuId(globalSkuId);
        return binSku;
    }

    // test for adding binSku
    @Test
    public void testAddBinSku() {
        newBinSku = binSkuApi.add(binSku);
        assertNotNull(newBinSku);
    }

    // test for getting binSku by id
    @Test
    public void testGetBinSkuDetailsById() {
        newBinSku = binSkuApi.add(binSku);
        BinSku binSku1 = binSkuApi.get(newBinSku.getId());
        assertNotNull(binSku1);
        // test retrieved data
        assertEquals(newBinSku.getBinId(),binSku1.getBinId());
        assertEquals(newBinSku.getQuantity(),binSku1.getQuantity());
        assertEquals(newBinSku.getGlobalSkuId(),binSku1.getGlobalSkuId());
    }

    // test for getting all binSku
    @Test
    public void testGetAllBinSku() {
        binSkuApi.add(binSku);
        List<BinSku> list = binSkuApi.getAll();
        assertNotNull(list);
        // test list size
        assertEquals(1, list.size());
    }

    // test for get binSku data bu id
    @Test
    public void testGetBinSkuData() {
        newBinSku = binSkuApi.add(binSku);
        BinSku binSku1 = binSkuApi.getBinSkuByBinIdGlobalSkuId(newBinSku.getBinId(), newBinSku.getGlobalSkuId());
        assertNotNull(binSku1);
        binSku1 = binSkuApi.getBinSkuByBinIdGlobalSkuId(newBinSku.getBinId(), newBinSku.getGlobalSkuId()+1);
        assertNull(binSku1);
    }

    // test for updating binSku
    @Test
    public void testUpdateBinSku() {
        newBinSku = binSkuApi.add(binSku);
        // update
        newBinSku.setBinId(1001L);
        newBinSku.setQuantity(6L);
        newBinSku.setGlobalSkuId(5L);
        newBinSku = binSkuApi.update(binSku.getId(),newBinSku);
        // test data
        assertEquals(1001,newBinSku.getBinId().intValue());
        assertEquals(6,newBinSku.getQuantity().intValue());
        assertEquals(5,newBinSku.getGlobalSkuId().intValue());
    }

    // test for getCheck
    @Test(expected = ApiException.class)
    public void testGetCheck(){
        newBinSku = binSkuApi.add(binSku);
        BinSku binSku1 = binSkuApi.getCheck(newBinSku.getId());
        assertNotNull(binSku1);
        assertEquals(newBinSku.getBinId(),binSku1.getBinId());
        assertEquals(newBinSku.getQuantity(),binSku1.getQuantity());
        assertEquals(newBinSku.getGlobalSkuId(),binSku1.getGlobalSkuId());
        // throw exception
        binSkuApi.getCheck(newBinSku.getId()+1);

    }

    // test for search
    @Test
    public void testSearch(){
        newBinSku = binSkuApi.add(binSku);
        List<Long> globalSkuIdList = new ArrayList<>();
        // add wrong id
        globalSkuIdList.add(newBinSku.getGlobalSkuId()+1);
        List<BinSku> binSkuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        // zero list size
        assertEquals(0,binSkuList.size());
        // add correct id
        globalSkuIdList.add(newBinSku.getGlobalSkuId());
        binSkuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        // 1 list size
        assertEquals(1,binSkuList.size());
    }

}
