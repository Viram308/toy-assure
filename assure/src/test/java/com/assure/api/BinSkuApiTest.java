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

    @Test
    public void testAddBinSku() {
        newBinSku = binSkuApi.add(binSku);
        assertNotNull(newBinSku);
    }

    @Test
    public void testGetBinSkuDetailsById() {
        newBinSku = binSkuApi.add(binSku);
        BinSku binSku1 = binSkuApi.get(newBinSku.getId());
        assertNotNull(binSku1);
        assertEquals(newBinSku.getBinId(),binSku1.getBinId());
        assertEquals(newBinSku.getQuantity(),binSku1.getQuantity());
        assertEquals(newBinSku.getGlobalSkuId(),binSku1.getGlobalSkuId());
    }

    @Test
    public void testGetAllBinSku() {
        binSkuApi.add(binSku);
        List<BinSku> list = binSkuApi.getAll();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetBinSkuData() {
        newBinSku = binSkuApi.add(binSku);
        BinSku binSku1 = binSkuApi.getBinSkuByBinIdGlobalSkuId(newBinSku.getBinId(), newBinSku.getGlobalSkuId());
        assertNotNull(binSku1);
        binSku1 = binSkuApi.getBinSkuByBinIdGlobalSkuId(newBinSku.getBinId(), newBinSku.getGlobalSkuId()+1);
        assertNull(binSku1);
    }

    @Test()
    public void testUpdateBinSku() {
        newBinSku = binSkuApi.add(binSku);
        newBinSku.setBinId(1001L);
        newBinSku.setQuantity(6L);
        newBinSku.setGlobalSkuId(5L);
        newBinSku = binSkuApi.update(binSku.getId(),newBinSku);
        assertEquals(1001,newBinSku.getBinId().intValue());
        assertEquals(6,newBinSku.getQuantity().intValue());
        assertEquals(5,newBinSku.getGlobalSkuId().intValue());
    }

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

    @Test
    public void testSearch(){
        newBinSku = binSkuApi.add(binSku);
        List<Long> globalSkuIdList = new ArrayList<>();
        globalSkuIdList.add(newBinSku.getGlobalSkuId()+1);
        List<BinSku> binSkuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        assertEquals(0,binSkuList.size());
        globalSkuIdList.add(newBinSku.getGlobalSkuId());
        binSkuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        assertEquals(1,binSkuList.size());
    }

}
