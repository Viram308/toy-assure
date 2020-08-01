package com.assure.util;

import com.assure.model.form.BinSkuForm;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderItemForm;
import com.commons.response.OrderData;
import com.commons.response.OrderItemData;
import com.assure.pojo.*;
import com.commons.form.ClientForm;
import com.commons.form.ProductForm;
import com.assure.model.response.BinSkuData;
import com.assure.model.response.InventoryData;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.response.ProductData;

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

    public static ClientData convertClientToClientData(Client client) {
        ClientData clientData = new ClientData();
        clientData.setId(client.getId());
        clientData.setName(client.getName());
        clientData.setType(client.getType().toString());
        return clientData;
    }

    public static OrderData convertOrderToOrderData(Order order, Client client, Client customer, ChannelData channelData) {
        OrderData orderData = new OrderData();
        orderData.setOrderId(order.getId());
        orderData.setClientName(client.getName());
        orderData.setCustomerName(customer.getName());
        orderData.setChannelName(channelData.getName());
        orderData.setChannelOrderId(order.getChannelOrderId());
        orderData.setStatus(order.getStatus().toString());
        return orderData;
    }

    public static Order convertOrderCsvFormToOrder(OrderCsvForm orderCsvForm) {
        Order order = new Order();
        if(!orderCsvForm.getOrderItemFormList().isEmpty()){
            OrderItemForm orderItemForm = orderCsvForm.getOrderItemFormList().get(0);
            order.setChannelId(orderItemForm.getChannelId());
            order.setClientId(orderItemForm.getClientId());
            order.setCustomerId(orderItemForm.getCustomerId());
            order.setChannelOrderId(orderItemForm.getChannelOrderId());
            return order;
        }
        return null;
    }

    public static OrderItemData convertOrderItemToOrderItemData(OrderItem orderItem,Product product){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setClientSkuId(product.getClientSkuId());
        orderItemData.setProductName(product.getName());
        orderItemData.setBrandId(product.getBrandId());
        orderItemData.setOrderedQuantity(orderItem.getOrderedQuantity());
        orderItemData.setSellingPricePerUnit(orderItem.getSellingPricePerUnit());
        return orderItemData;
    }

    public static OrderItem convertFormToOrderItemPojo(OrderItemForm orderItemForm, Long globalSkuId, Long orderId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setGlobalSkuId(globalSkuId);
        orderItem.setOrderedQuantity(orderItemForm.getOrderedQuantity());
        orderItem.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
        orderItem.setAllocatedQuantity(0L);
        orderItem.setFulfilledQuantity(0L);
        return orderItem;
    }
}
