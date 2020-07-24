package com.assure.util;

import com.assure.model.form.ClientForm;
import com.assure.model.form.ProductForm;
import com.assure.model.response.ProductData;
import com.assure.pojo.Client;
import com.assure.pojo.Product;

public class ConverterUtil {

    public static Client convertClientFormToClient(ClientForm clientForm){
        Client client = new Client();
        client.setName(clientForm.getName());
        client.setType(clientForm.getType());
        return client;
    }

    public static ProductData convertProductToProductData(Product product, Client client) {
        ProductData productData = new ProductData();
        productData.setBrandId(product.getBrandId());
        productData.setClientName(client.getName());
        productData.setClientSkuId(product.getClientSkuId());
        productData.setDescription(product.getDescription());
        productData.setGlobalSkuId(product.getGlobalSkuId());
        productData.setMrp(product.getMrp());
        productData.setProductName(product.getName());
        return productData;
    }

    public static Product convertProductFormToProduct(ProductForm productForm) {
        Product product = new Product();
        product.setBrandId(productForm.getBrandId());
        product.setName(productForm.getProductName());
        product.setMrp(productForm.getMrp());
        product.setDescription(productForm.getDescription());
        product.setClientSkuId(productForm.getClientSkuId());
        product.setClientId(productForm.getClientId());
        return product;
    }
}
