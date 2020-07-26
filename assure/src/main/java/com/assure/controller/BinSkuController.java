package com.assure.controller;

import com.assure.dto.BinSkuDto;
import com.assure.model.form.BinSkuCsvForm;
import com.assure.model.form.BinSkuSearchForm;
import com.assure.model.form.BinSkuUpdateForm;
import com.assure.model.response.BinSkuData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/binSku")
public class BinSkuController {
    private static final Logger logger = Logger.getLogger(BinSkuController.class);

    @Autowired
    private BinSkuDto binSkuDto;
    // CRUD operations for client

    @ApiOperation(value = "Adds binSku")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void addBinSku(@RequestBody BinSkuCsvForm binSkuCsvForm, BindingResult result) {
        logger.info("adding bin sku");
        binSkuDto.addBinSku(binSkuCsvForm, result);
    }

    @ApiOperation(value = "Gets all BinSku")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BinSkuData> getAllBinSku() {
        logger.info("get all binSku");
        return binSkuDto.getAllBinSku();
    }

    @ApiOperation(value = "Gets binSku by binSkuId")
    @RequestMapping(value = "/{binSkuId}", method = RequestMethod.GET)
    public BinSkuData getBinSkuByBinSkuId(@PathVariable Long binSkuId) {
        logger.info("get binSku for binSkuId : " + binSkuId);
        return binSkuDto.getBinSku(binSkuId);
    }

    @ApiOperation(value = "Search binSku")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<BinSkuData> searchBinSku(@RequestBody BinSkuSearchForm binSkuSearchForm) {
        logger.info("search binSku");
        return binSkuDto.searchBinSku(binSkuSearchForm);
    }

    @ApiOperation(value = "Update binSku")
    @RequestMapping(value = "/{binSkuId}", method = RequestMethod.PUT)
    public void updateBinSku(@PathVariable Long binSkuId,@RequestBody BinSkuUpdateForm binSkuUpdateForm) {
        logger.info("update binSku");
        binSkuDto.updateBinSkuInventory(binSkuId,binSkuUpdateForm);
    }


}
