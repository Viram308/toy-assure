package com.assure.dto;

import com.assure.api.BinSkuApi;
import com.assure.api.InventoryApi;
import com.assure.api.ProductApi;
import com.assure.model.form.BinSkuCsvForm;
import com.assure.model.form.BinSkuForm;
import com.assure.model.form.BinSkuSearchForm;
import com.assure.model.form.BinSkuUpdateForm;
import com.assure.model.response.BinSkuData;
import com.assure.pojo.BinSku;
import com.assure.pojo.Inventory;
import com.assure.pojo.Product;
import com.assure.util.ConverterUtil;
import com.assure.validator.BinSkuCsvFormValidator;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BinSkuDto {

    private static final Logger logger = Logger.getLogger(BinSkuDto.class);

    @Autowired
    private BinSkuCsvFormValidator binSkuCsvFormValidator;

    @Autowired
    private BinSkuApi binSkuApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    @Transactional(rollbackFor = CustomValidationException.class)
    public void addBinSku(BinSkuCsvForm binSkuCsvForm, BindingResult result) {
        binSkuCsvFormValidator.validate(binSkuCsvForm, result);
        logger.info("Errors in binSkuCsv : " + result.getErrorCount());
        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }
        logger.info("No errors in binInventory csv file");
        for (BinSkuForm binSkuForm : binSkuCsvForm.getBinSkuFormList()) {
            Product product = productApi.getByClientIdAndClientSkuId(binSkuForm.getClientId(), binSkuForm.getClientSkuId());
            BinSku binSku = binSkuApi.getBinSkuByBinIdGlobalSkuId(binSkuForm.getBinId(), product.getGlobalSkuId());
            if (binSku == null) {
                BinSku binSkuToInsert = ConverterUtil.convertBinSkuFormToBinSku(binSkuForm, product);
                addNewBinSku(binSkuToInsert);
            } else {
                updateBinSku(binSkuForm, binSku);
            }
        }
        logger.info("binSkuList added");
    }

    private void updateBinSku(BinSkuForm binSkuForm, BinSku binSku) {
        Long quantity = binSkuForm.getQuantity() + binSku.getQuantity();
        BinSku binSkuUpdate = new BinSku();
        binSkuUpdate.setQuantity(quantity);
        binSkuApi.update(binSku.getId(), binSkuUpdate);
        updateInventory(binSku, binSkuForm.getQuantity());
    }

    private void addNewBinSku(BinSku binSku) {
        binSkuApi.add(binSku);
        updateInventory(binSku, binSku.getQuantity());
    }

    private void updateInventory(BinSku binSku, Long quantity) {
        Inventory inventory = inventoryApi.getInventoryByGlobalSkuId(binSku.getGlobalSkuId());
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventoryApi.update(inventory.getId(), inventory);
    }


    @Transactional(readOnly = true)
    public List<BinSkuData> getAllBinSku() {
        List<BinSku> binSkuList = binSkuApi.getAll();
        return binSkuList.stream().map(o -> ConverterUtil.convertBinSkuToBinSkuData(o, productApi.get(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BinSkuData getBinSku(Long binSkuId) {
        BinSku binSku = binSkuApi.get(binSkuId);
        return ConverterUtil.convertBinSkuToBinSkuData(binSku,productApi.get(binSku.getGlobalSkuId()));
    }

    @Transactional(readOnly = true)
    public List<BinSkuData> searchBinSku(BinSkuSearchForm binSkuSearchForm) {
        List<Product> productList = productApi.search(binSkuSearchForm.getProductName());
        if(binSkuSearchForm.getClientId()!=0){
            productList = productList.stream().filter(o->(binSkuSearchForm.getClientId().equals(o.getClientId()))).collect(Collectors.toList());
        }
        List<Long> globalSkuIdList = productList.stream().map(Product::getGlobalSkuId).collect(Collectors.toList());
        if(globalSkuIdList.isEmpty()){
            return null;
        }
        List<BinSku> binSkuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        if(binSkuSearchForm.getBinId()==0){
            return binSkuList.stream().map(o -> ConverterUtil.convertBinSkuToBinSkuData(o, productApi.get(o.getGlobalSkuId()))).collect(Collectors.toList());
        }
        binSkuList = binSkuList.stream().filter(o->(binSkuSearchForm.getBinId().equals(o.getBinId()))).collect(Collectors.toList());
        return binSkuList.stream().map(o -> ConverterUtil.convertBinSkuToBinSkuData(o, productApi.get(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateBinSkuInventory(Long binSkuId, BinSkuUpdateForm binSkuUpdateForm) {
        Long quantityToAdd = Math.subtractExact(binSkuUpdateForm.getUpdateQuantity(),binSkuUpdateForm.getOriginalQuantity());
        BinSku binSku = new BinSku();
        binSku.setQuantity(binSkuUpdateForm.getUpdateQuantity());
        binSkuApi.update(binSkuId,binSku);
        Inventory inventory = inventoryApi.getInventoryByGlobalSkuId(binSkuUpdateForm.getGlobalSkuId());
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantityToAdd);
        inventoryApi.update(inventory.getId(),inventory);
        logger.info("Inventory updated");
    }
}
