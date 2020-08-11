package com.assure.dto;

import com.assure.api.ClientApi;
import com.assure.api.InventoryApi;
import com.assure.api.ProductApi;
import com.assure.model.form.InventorySearchForm;
import com.assure.model.response.InventoryData;
import com.assure.pojo.Client;
import com.assure.pojo.Inventory;
import com.assure.pojo.Product;
import com.assure.util.ConverterUtil;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryDto {

    private static final Logger logger = Logger.getLogger(InventoryDto.class);

    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ClientApi clientApi;

    @Transactional(readOnly = true)
    public List<InventoryData> getAllInventory() {
        List<Inventory> inventoryList = inventoryApi.getAll();
        return getInventoryDataList(inventoryList);
    }

    @Transactional(readOnly = true)
    public List<InventoryData> searchInventory(InventorySearchForm inventorySearchForm) {
        if (inventorySearchForm.getClientId() == 0) {
            return getAllInventory();
        }
        List<Long> globalSkuIdList;
        List<Product> productList = getProductList(inventorySearchForm);
        globalSkuIdList = productList.stream().map(Product::getGlobalSkuId).collect(Collectors.toList());
        List<Inventory> inventoryList = new ArrayList<>();
        if (globalSkuIdList.isEmpty()) {
            return getInventoryDataList(inventoryList);
        }
        inventoryList = inventoryApi.searchByGlobalSkuIdList(globalSkuIdList);
        return getInventoryDataList(inventoryList);
    }

    private List<Product> getProductList(InventorySearchForm inventorySearchForm) {
        List<Product> productList = new ArrayList<>();
        if (StringUtil.isEmpty(inventorySearchForm.getClientSkuId())) {
            productList = productApi.getByClientId(inventorySearchForm.getClientId());
        } else {
            Product product = productApi.getByClientIdAndClientSkuId(inventorySearchForm.getClientId(), inventorySearchForm.getClientSkuId());
            if (product != null) {
                productList.add(product);
            }
        }
        return productList;
    }

    private List<InventoryData> getInventoryDataList(List<Inventory> inventoryList) {
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Product product = productApi.get(inventory.getGlobalSkuId());
            Client client = clientApi.get(product.getClientId());
            InventoryData inventoryData = ConverterUtil.convertInventoryToInventoryData(inventory, product, client);
            inventoryDataList.add(inventoryData);
        }
        logger.info("InventoryDataList size : " + inventoryDataList.size());
        return inventoryDataList;
    }

}
