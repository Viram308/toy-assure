package com.assure.validator;

import com.assure.controller.ProductApiController;
import com.assure.dto.ClientDto;
import com.assure.dto.ProductDto;
import com.assure.model.form.ProductCsvForm;
import com.assure.model.form.ProductForm;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.commons.enums.ClientType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductCsvFormValidator implements Validator {
    @Autowired
    private ClientDto clientDto;

    @Autowired
    private ProductDto productDto;
    private static final Logger logger = Logger.getLogger(ProductCsvFormValidator.class);
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCsvFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCsvForm productCsvForm = (ProductCsvForm) target;
        List<ProductForm> productFormList = productCsvForm.getProductFormList();
        int index=0;
        HashMap<String,Integer> checkDuplicate = new HashMap<String, Integer>();
        logger.info("empty map");
        for(ProductForm productForm : productFormList){
            logger.info("checkData");
            Long clientId=productForm.getClientId();
            String productName=productForm.getProductName();
            String brandId=productForm.getBrandId();
            String clientSkuId=productForm.getClientSkuId();
            Client client = clientDto.getClient(clientId);
            logger.info("data collected");
            if(checkDuplicate.containsKey(clientSkuId)){
                errors.pushNestedPath("productFormList["+index+"]");
                errors.rejectValue("clientSkuId","duplicate","clientSkuId already present in csv file");
                errors.popNestedPath();
            }
            else{
                checkDuplicate.put(clientSkuId,1);
            }
            logger.info("check client");
            if(client==null || !client.getType().equals(ClientType.CLIENT)){
                errors.pushNestedPath("productFormList["+index+"]");
                errors.rejectValue("clientId","not found","Client doesn't exist for clientId :"+clientId);
                errors.popNestedPath();
            }
            logger.info("check product for clientId :"+clientId + " clientskuid : "+clientSkuId);
            Product product = productDto.getProductByClientIdAndClientSkuId(clientId,clientSkuId);
            logger.info("check null product");
            if(product != null){
                logger.info("null product");
                errors.pushNestedPath("productFormList["+index+"]");
                errors.rejectValue("clientSkuId","duplicate","product already exist for clientSkuId : "+clientSkuId+" and clientId :" +clientId);
                errors.popNestedPath();
            }
            logger.info("chack name");
            if(productName.equals("")) {
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("productName", "empty","Product Name cannot be empty.");
                errors.popNestedPath();
            }
            logger.info("check brand");
            if(brandId.equals("")) {
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("brandId", "empty","Product Brand ID cannot be empty.");
                errors.popNestedPath();
            }
            index++;
        }

    }
}
