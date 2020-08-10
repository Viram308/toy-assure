package com.assure.util;

import com.assure.model.form.BinSkuForm;
import com.assure.model.response.BinSkuData;
import com.assure.model.response.InventoryData;
import com.assure.pojo.*;
import com.assure.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.enums.InvoiceType;
import com.commons.enums.OrderStatus;
import com.commons.form.ClientForm;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderItemForm;
import com.commons.form.ProductForm;
import com.commons.response.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConverterUtilTest extends AbstractUnitTest {
    private ClientForm clientForm;
    private ProductForm productForm;
    private BinSkuForm binSkuForm;
    private OrderItemForm orderItemForm;
    private OrderCsvForm orderCsvForm;
    private ChannelData channelData;

    @Before
    public void setUp() {
        clientForm = createClientForm();
        productForm = createProductForm();
        binSkuForm = createBinSkuForm();
        orderItemForm = createOrderItemForm();
        orderCsvForm = createOrderCsvForm();
        channelData=createChannelData();
    }

    private ChannelData createChannelData() {
        ChannelData channelData = new ChannelData();
        channelData.setId(1L);
        channelData.setName("channel1");
        channelData.setInvoiceType(InvoiceType.CHANNEL.toString());
        return channelData;
    }

    private OrderCsvForm createOrderCsvForm(){
        OrderCsvForm orderCsvForm = new OrderCsvForm();
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = createOrderItemForm();
        orderItemFormList.add(orderItemForm);
        orderCsvForm.setOrderItemFormList(orderItemFormList);
        return orderCsvForm;
    }

    private OrderItemForm createOrderItemForm() {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setChannelId(1L);
        orderItemForm.setChannelOrderId("o1");
        orderItemForm.setClientId(2L);
        orderItemForm.setClientSkuId("prod1");
        orderItemForm.setCustomerId(3L);
        orderItemForm.setOrderedQuantity(20L);
        orderItemForm.setSellingPricePerUnit(20.50);
        return orderItemForm;
    }

    private ClientForm createClientForm() {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("testClient");
        clientForm.setType(ClientType.CLIENT);
        return clientForm;
    }
    private ProductForm createProductForm(){
        ProductForm productForm = new ProductForm();
        productForm.setClientId(1L);
        productForm.setBrandId("nestle");
        productForm.setClientSkuId("prod1");
        productForm.setDescription("nice");
        productForm.setMrp(20.50);
        productForm.setProductName("munch");
        return productForm;
    }

    private BinSkuForm createBinSkuForm() {
        BinSkuForm binSkuForm = new BinSkuForm();
        binSkuForm.setBinId(1001L);
        binSkuForm.setQuantity(20L);
        return binSkuForm;
    }
    // tests for converting form to data and data to entity


    @Test
    public void testConvertClientFormToClient(){
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        assertNotNull(client);
        assertEquals(clientForm.getName(),client.getName());
        assertEquals(clientForm.getType(),client.getType());
    }

    @Test
    public void testConvertProductFormToProduct(){
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        assertNotNull(product);
        assertEquals(productForm.getBrandId(),product.getBrandId());
        assertEquals(productForm.getClientId(),product.getClientId());
        assertEquals(productForm.getClientSkuId(),product.getClientSkuId());
        assertEquals(productForm.getDescription(),product.getDescription());
        assertEquals(productForm.getProductName(),product.getName());
        assertEquals(productForm.getMrp(),product.getMrp(),0.01);
    }

    @Test
    public void testConvertProductToProductData(){
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        product.setGlobalSkuId(2L);
        ProductData productData = ConverterUtil.convertProductToProductData(product,client);
        assertNotNull(productData);
        assertEquals(product.getBrandId(),productData.getBrandId());
        assertEquals(client.getName(),productData.getClientName());
        assertEquals(product.getClientSkuId(),productData.getClientSkuId());
        assertEquals(product.getDescription(),productData.getDescription());
        assertEquals(product.getGlobalSkuId(),productData.getGlobalSkuId());
        assertEquals(product.getName(),productData.getProductName());
        assertEquals(product.getMrp(),productData.getMrp(),0.01);
    }

    @Test
    public void testConvertBinSkuFormToBinSku(){
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        product.setGlobalSkuId(2L);
        BinSku binSku = ConverterUtil.convertBinSkuFormToBinSku(binSkuForm,product);
        assertNotNull(binSku);
        assertEquals(binSkuForm.getBinId(),binSku.getBinId());
        assertEquals(binSkuForm.getQuantity(),binSku.getQuantity());
        assertEquals(product.getGlobalSkuId(),binSku.getGlobalSkuId());
    }

    @Test
    public void testConvertProductToInventory(){
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        product.setGlobalSkuId(2L);
        Inventory inventory = ConverterUtil.convertProductToInventory(product);
        assertEquals(product.getGlobalSkuId(),inventory.getGlobalSkuId());
        assertEquals(0,inventory.getAvailableQuantity().intValue());
        assertEquals(0,inventory.getAllocatedQuantity().intValue());
        assertEquals(0,inventory.getFulfilledQuantity().intValue());
    }

    @Test
    public void testConvertBinSkuToBinSkuData(){
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        product.setGlobalSkuId(2L);
        BinSku binSku = ConverterUtil.convertBinSkuFormToBinSku(binSkuForm,product);
        binSku.setId(3L);
        BinSkuData binSkuData = ConverterUtil.convertBinSkuToBinSkuData(binSku,product,client);
        assertNotNull(binSku);
        assertEquals(binSku.getBinId(),binSkuData.getBinId());
        assertEquals(binSku.getId(),binSkuData.getBinSkuId());
        assertEquals(product.getBrandId(),binSkuData.getBrandId());
        assertEquals(product.getGlobalSkuId(),binSkuData.getGlobalSkuId());
        assertEquals(product.getName(),binSkuData.getProductName());
        assertEquals(binSku.getQuantity(),binSkuData.getQuantity());
        assertEquals(client.getName(),binSkuData.getClientName());
    }

    @Test
    public void testConvertInventoryToInventoryData(){
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        Inventory inventory = ConverterUtil.convertProductToInventory(product);
        InventoryData inventoryData = ConverterUtil.convertInventoryToInventoryData(inventory,product,client);
        assertNotNull(inventoryData);
        assertEquals(inventory.getAvailableQuantity(),inventoryData.getAvailableQuantity());
        assertEquals(inventory.getAllocatedQuantity(),inventoryData.getAllocatedQuantity());
        assertEquals(inventory.getFulfilledQuantity(),inventoryData.getFulfilledQuantity());
        assertEquals(product.getBrandId(),inventoryData.getBrandId());
        assertEquals(product.getName(),inventoryData.getProductName());
        assertEquals(client.getName(),inventoryData.getClientName());
    }

    @Test
    public void testConvertClientToClientData(){
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        client.setId(1L);
        ClientData clientData = ConverterUtil.convertClientToClientData(client);
        assertNotNull(clientData);
        assertEquals(client.getId(),clientData.getId());
        assertEquals(client.getName(),clientData.getName());
        assertEquals(client.getType().toString(),clientData.getType());
    }

    @Test
    public void testConvertFormToOrderItemPojo(){
        OrderItem orderItem = ConverterUtil.convertFormToOrderItemPojo(orderItemForm,3L,6L);
        assertNotNull(orderItem);
        assertEquals(6,orderItem.getOrderId().intValue());
        assertEquals(3,orderItem.getGlobalSkuId().intValue());
        assertEquals(orderItemForm.getOrderedQuantity(),orderItem.getOrderedQuantity());
        assertEquals(orderItemForm.getSellingPricePerUnit(),orderItem.getSellingPricePerUnit(),0.01);
        assertEquals(0,orderItem.getAllocatedQuantity().intValue());
        assertEquals(0,orderItem.getFulfilledQuantity().intValue());
    }

    @Test
    public void testConvertOrderItemToOrderItemData(){
        OrderItem orderItem = ConverterUtil.convertFormToOrderItemPojo(orderItemForm,3L,6L);
        Product product = ConverterUtil.convertProductFormToProduct(productForm);
        OrderItemData orderItemData = ConverterUtil.convertOrderItemToOrderItemData(orderItem,product);
        assertNotNull(orderItemData);
        assertEquals(product.getBrandId(),orderItemData.getBrandId());
        assertEquals(product.getClientSkuId(),orderItemData.getClientSkuId());
        assertEquals(product.getName(),orderItemData.getProductName());
        assertEquals(orderItem.getOrderedQuantity(),orderItemData.getOrderedQuantity());
        assertEquals(orderItem.getSellingPricePerUnit(),orderItemData.getSellingPricePerUnit(),0.01);
        assertEquals(orderItem.getAllocatedQuantity(),orderItemData.getAllocatedQuantity());
    }

    @Test
    public void testConvertOrderCsvFormToOrder(){
        Order order = ConverterUtil.convertOrderCsvFormToOrder(orderCsvForm);
        assertNotNull(order);
        OrderItemForm orderItemForm = orderCsvForm.getOrderItemFormList().get(0);
        assertEquals(orderItemForm.getChannelId(),order.getChannelId());
        assertEquals(orderItemForm.getChannelOrderId(),order.getChannelOrderId());
        assertEquals(orderItemForm.getClientId(),order.getClientId());
        assertEquals(orderItemForm.getCustomerId(),order.getCustomerId());
        orderCsvForm.getOrderItemFormList().clear();
        order = ConverterUtil.convertOrderCsvFormToOrder(orderCsvForm);
        assertNull(order);
    }

    @Test
    public void testConvertOrderToOrderData(){
        orderCsvForm = createOrderCsvForm();
        Order order = ConverterUtil.convertOrderCsvFormToOrder(orderCsvForm);
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        client.setId(5L);
        Client customer = ConverterUtil.convertClientFormToClient(clientForm);
        customer.setName("testCustomer");
        assert order != null;
        order.setStatus(OrderStatus.CREATED);
        OrderData orderData = ConverterUtil.convertOrderToOrderData(order,client,customer,channelData);
        assertNotNull(orderData);
        assertEquals(channelData.getId(),orderData.getChannelId());
        assertEquals(channelData.getName(),orderData.getChannelName());
        assertEquals(order.getChannelOrderId(),orderData.getChannelOrderId());
        assertEquals(client.getId(),orderData.getClientId());
        assertEquals(client.getName(),orderData.getClientName());
        assertEquals(customer.getName(),orderData.getCustomerName());
        assertEquals(order.getId(),orderData.getOrderId());
        assertEquals(order.getStatus().toString(),orderData.getStatus());

    }

}
