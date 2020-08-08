package com.assure.controller;

import com.assure.dto.ProductDto;
import com.commons.form.ProductCsvForm;
import com.commons.form.ProductForm;
import com.commons.form.ProductSearchForm;
import com.commons.response.ProductData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/product")
public class ProductApiController {

    private static final Logger logger = Logger.getLogger(ProductApiController.class);
    @Autowired
    private ProductDto productDto;
    // CRUD operations for client

    @ApiOperation(value = "Adds products")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<ProductData> addProducts(@RequestBody ProductCsvForm productCsvForm, BindingResult result) throws Exception {
        logger.info("adding products");
        return productDto.addProducts(productCsvForm,result);
    }

    @ApiOperation(value = "Gets products by globalSkuId")
    @RequestMapping(value = "/{globalSkuId}", method = RequestMethod.GET)
    public ProductData getProductByGlobalSkuId(@PathVariable Long globalSkuId) {
        logger.info("get product for globalSkuId : "+globalSkuId);
        return productDto.getProduct(globalSkuId);
    }

    @ApiOperation(value = "Search Products")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<ProductData> search(@RequestBody ProductSearchForm productSearchForm) {
        logger.info("search products");
        return productDto.searchProducts(productSearchForm);
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(value = "/{globalSkuId}", method = RequestMethod.PUT)
    public ProductData updateProduct(@PathVariable Long globalSkuId, @RequestBody ProductForm productForm) {
        logger.info("update product for globalSkuId : "+globalSkuId);
        return productDto.updateProduct(globalSkuId, productForm);
    }

    @ApiOperation(value = "Gets all Products")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductData> getAllProducts() {
        logger.info("get all products");
        return productDto.getAllProducts();
    }

    @ApiOperation(value = "Gets all clientSkuIds by ClientId")
    @RequestMapping(value = "/getClientSku/{clientId}", method = RequestMethod.GET)
    public List<String> getClientSkuByClient(@PathVariable Long clientId) {
        logger.info("get clientSkuIds");
        return productDto.getClientSkuIds(clientId);
    }

}
