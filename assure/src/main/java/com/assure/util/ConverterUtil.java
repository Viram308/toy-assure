package com.assure.util;

import com.assure.model.form.BinSkuForm;
import com.assure.model.form.ClientForm;
import com.assure.model.form.ProductForm;
import com.assure.model.response.BinSkuData;
import com.assure.model.response.InventoryData;
import com.assure.model.response.ProductData;
import com.assure.pojo.BinSku;
import com.assure.pojo.Client;
import com.assure.pojo.Inventory;
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

    public static BinSku convertBinSkuFormToBinSku(BinSkuForm binSkuForm,Product product) {
        BinSku binSku = new BinSku();
        binSku.setBinId(binSkuForm.getBinId());
        binSku.setQuantity(binSkuForm.getQuantity());
        binSku.setGlobalSkuId(product.getGlobalSkuId());
        return binSku;
    }

    public static Inventory convertProductToInventory(Product product) {
        Inventory inventory =new Inventory();
        inventory.setGlobalSkuId(product.getGlobalSkuId());
        inventory.setAvailableQuantity(0L);
        inventory.setAllocatedQuantity(0L);
        inventory.setFulfilledQuantity(0L);
        return inventory;

    }

    public static BinSkuData convertBinSkuToBinSkuData(BinSku binSku, Product product) {
        BinSkuData binSkuData = new BinSkuData();
        binSkuData.setBinId(binSku.getBinId());
        binSkuData.setBinSkuId(binSku.getId());
        binSkuData.setQuantity(binSku.getQuantity());
        binSkuData.setGlobalSkuId(product.getGlobalSkuId());
        binSkuData.setProductName(product.getName());
        binSkuData.setBrandId(product.getBrandId());
        return binSkuData;
    }

    public static InventoryData convertInventoryToInventoryData(Inventory inventory,Product product,Client client) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setClientName(client.getName());
        inventoryData.setProductName(product.getName());
        inventoryData.setBrandId(product.getBrandId());
        inventoryData.setAvailableQuantity(inventory.getAvailableQuantity());
        inventoryData.setAllocatedQuantity(inventory.getAllocatedQuantity());
        inventoryData.setFulfilledQuantity(inventory.getFulfilledQuantity());
        return inventoryData;
    }
}
