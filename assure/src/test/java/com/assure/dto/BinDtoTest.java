package com.assure.dto;

import com.assure.model.form.BinForm;
import com.assure.pojo.Bin;
import com.assure.spring.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BinDtoTest extends AbstractUnitTest {
    private BinForm binForm1,binForm2;

    @Autowired
    private BinDto binDto;

    @Before
    public void setUp(){
        binForm1 = createBinForm(1L);
        binForm2 = createBinForm(1L);
    }

    private BinForm createBinForm(Long noOfBins){
        BinForm binForm = new BinForm();
        binForm.setNoOfBins(noOfBins);
        return binForm;
    }

    @Test
    public void testAddBins(){
        List<Long> binIdList= binDto.addBins(binForm1);
        assertEquals(binForm1.getNoOfBins().longValue(),binIdList.size());
    }

    @Test
    public void testGetBin(){
        binDto.addBins(binForm1);
        List<Bin> binList = binDto.getAllBins();
        Bin bin = binDto.getBin(binList.get(0).getBinId());
        assertNotNull(bin);
        bin = binDto.getBin(binList.get(0).getBinId()+1);
        assertNull(bin);
    }

    @Test
    public void testGetAll(){
        binDto.addBins(binForm1);
        binDto.addBins(binForm2);
        List<Bin> binList = binDto.getAllBins();
        assertEquals(2,binList.size());
    }

}
