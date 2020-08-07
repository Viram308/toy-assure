package com.assure.controller;

import com.assure.dto.BinDto;
import com.assure.model.form.BinForm;
import com.assure.pojo.Bin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/bin")
public class BinController {

    private static final Logger logger = Logger.getLogger(BinController.class);

    @Autowired
    private BinDto binDto;
    // CRUD operations for client

    @ApiOperation(value = "Adds bins")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<Long> addBins(@RequestBody BinForm binForm) {
        logger.info("adding bins");
        return binDto.addBins(binForm);
    }

    @ApiOperation(value = "Gets all Bins")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Bin> getAllBins() {
        logger.info("get all bins");
        return binDto.getAllBins();
    }

}
