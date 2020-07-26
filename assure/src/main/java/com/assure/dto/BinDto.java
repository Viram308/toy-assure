package com.assure.dto;

import com.assure.api.BinApi;
import com.assure.model.form.BinForm;
import com.assure.pojo.Bin;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BinDto {

    private static final Logger logger = Logger.getLogger(BinDto.class);
    @Autowired
    private BinApi binApi;

    @Transactional
    public Long addBins(BinForm binForm) {
        long i,noOfBins = binForm.getNoOfBins();
        for(i=0;i<noOfBins;i++){
            binApi.add(new Bin());
        }
        logger.info("No. of bins added :"+noOfBins);
        return noOfBins;
    }

    @Transactional(readOnly = true)
    public Bin getBin(Long binId){
        return binApi.getBin(binId);
    }

    @Transactional(readOnly = true)
    public List<Bin> getAllBins() {
        return binApi.getAll();
    }
}
