package com.assure.util;

import com.assure.pojo.Client;
import com.assure.pojo.Order;
import com.assure.pojo.OrderItem;
import com.assure.pojo.Product;
import com.assure.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalizeUtilTest extends AbstractUnitTest {
    private Client client;
    private Product product;
    private Order order;
    private OrderItem orderItem;

    @Before
    public void setUp() {
        client = createClient();
        product = createProduct();
        order = createOrder();
        orderItem = createOrderItem();
    }

    private OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setSellingPricePerUnit(20.916);
        return orderItem;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setChannelOrderId("CHannelOrder1       ");
        return order;
    }

    private Product createProduct() {
        Product product = new Product();
        product.setName("muNch");
        product.setClientId(1L);
        product.setBrandId("Nestle    ");
        product.setClientSkuId("Prod1");
        product.setMrp(20.50);
        product.setDescription(" nIce ");
        return product;
    }

    private Client createClient() {
        Client client = new Client();
        client.setName("testClient");
        client.setType(ClientType.CLIENT);
        return client;
    }

    @Test
    public void testNormalizeClient(){
        NormalizeUtil.normalizeClient(client);
        assertEquals("testclient",client.getName());
        assertEquals(ClientType.CLIENT,client.getType());
    }

    @Test
    public void testNormalizeProduct(){
        NormalizeUtil.normalizeProduct(product);
        assertEquals("prod1",product.getClientSkuId());
        assertEquals("munch",product.getName());
        assertEquals("nice",product.getDescription());
        assertEquals("nestle",product.getBrandId());
    }

    @Test
    public void testNormalizeOrder(){
        NormalizeUtil.normalizeOrder(order);
        assertEquals("channelorder1",order.getChannelOrderId());
    }

    @Test
    public void testNormalizeOrderItem(){
        NormalizeUtil.normalizeOrderItem(orderItem);
        assertEquals(20.92,orderItem.getSellingPricePerUnit(),0.01);
    }


}
