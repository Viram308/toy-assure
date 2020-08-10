package com.assure.api;

import com.assure.pojo.Bin;
import com.assure.spring.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class BinApiTest extends AbstractUnitTest {
    private Bin bin;
    @Autowired
    private BinApi binApi;

    @Before
    public void setUp(){
        bin = new Bin();
    }

    // test for addition of bins
    @Test
    public void testAdd() {
        binApi.add(bin);
        assertTrue(bin.getBinId()>=1000);
    }

    //test for getting all bins
    @Test
    public void testGetAllBins() {
        binApi.add(bin);
        List<Bin> list = binApi.getAll();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    // test for getting one bin
    @Test
    public void testGetBin() {
        binApi.add(bin);
        Bin result = binApi.getBin(bin.getBinId());
        assertNotNull(result);
    }
}
