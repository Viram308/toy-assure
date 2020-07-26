package com.assure.controller;

import com.assure.dto.InventoryDto;
import com.assure.model.form.InventorySearchForm;
import com.assure.model.response.InventoryData;
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
@RequestMapping(value = "/api/inventory")
public class InventoryController {

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Gets all Inventory")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<InventoryData> getAllInventory() {
        logger.info("get all inventory");
        return inventoryDto.getAllInventory();
    }

    @ApiOperation(value = "Search Inventory")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<InventoryData> searchInventory(@RequestBody InventorySearchForm inventorySearchForm) {
        logger.info("search inventory");
        return inventoryDto.searchInventory(inventorySearchForm);
    }

}
