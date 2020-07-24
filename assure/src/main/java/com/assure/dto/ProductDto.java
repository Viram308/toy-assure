package com.assure.dto;

import com.assure.api.ClientApi;
import com.assure.api.ProductApi;
import com.assure.model.form.ProductCsvForm;
import com.assure.model.form.ProductForm;
import com.assure.model.form.ProductSearchForm;
import com.assure.model.response.ProductData;
import com.assure.pojo.Product;
import com.assure.util.ConverterUtil;
import com.assure.validator.ProductCsvFormValidator;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDto {

    private static final Logger logger = Logger.getLogger(ProductDto.class);
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ClientApi clientApi;
    @Autowired
    private ProductCsvFormValidator productCsvFormValidator;

    public List<ProductData> addProducts(ProductCsvForm productCsvForm, BindingResult result) {
        productCsvFormValidator.validate(productCsvForm, result);
        logger.info("validated");
        if (result.hasErrors()) {
            logger.info(result.getErrorCount());
            throw new CustomValidationException(result);
        }
        logger.info("No errors");
        List<Product> productList = new ArrayList<>();
        for(ProductForm productForm : productCsvForm.getProductFormList()){
            Product product = ConverterUtil.convertProductFormToProduct(productForm);
            logger.info(productForm.getClientId());
            logger.info(product.getClientSkuId());

            productList.add(productApi.add(product));
        }
        logger.info("products added:"+productList.get(0).getClientId());
        return productList.stream().map(o->ConverterUtil.convertProductToProductData(o,clientApi.get(o.getClientId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductData getProduct(Long globalSkuId) {
        Product product = productApi.get(globalSkuId);
        return ConverterUtil.convertProductToProductData(product,clientApi.get(product.getClientId()));
    }

    @Transactional(readOnly = true)
    public List<ProductData> searchProducts(ProductSearchForm productSearchForm) {
        List<Product> productList = productApi.search(productSearchForm);
        if(productSearchForm.getClientId() == 0) {
            return productList.stream().map(o -> ConverterUtil.convertProductToProductData(o, clientApi.get(o.getClientId()))).collect(Collectors.toList());
        }
        productList = productList.stream().filter(o->(productSearchForm.getClientId().equals(o.getClientId()))).collect(Collectors.toList());
        return productList.stream().map(o -> ConverterUtil.convertProductToProductData(o, clientApi.get(o.getClientId()))).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ApiException.class)
    public ProductData updateProduct(Long globalSkuId, ProductForm productForm) {
        validate(productForm);
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        product = productApi.update(globalSkuId,product);
        logger.info("Product updated");
        return ConverterUtil.convertProductToProductData(product,clientApi.get(product.getClientId()));
    }

    @Transactional(readOnly = true)
    public List<ProductData> getAllProducts() {
        List<Product> productList = productApi.getAll();
        return productList.stream().map(o->ConverterUtil.convertProductToProductData(o,clientApi.get(o.getClientId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Product getProductByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
        return productApi.getByClientIdAndClientSkuId(clientId,clientSkuId);
    }

    public void validate(ProductForm productForm){
        if(StringUtil.isEmpty(productForm.getBrandId()) || StringUtil.isEmpty(productForm.getProductName()) || productForm.getMrp() <= 0){
            throw new ApiException("Please enter brandId,productName and valid MRP !!");
        }
    }
}
