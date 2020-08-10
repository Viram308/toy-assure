package com.assure.api;

import com.assure.pojo.Order;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.enums.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class OrderApiTest extends AbstractUnitTest {
    private Order order1, order2;
    @Autowired
    private OrderApi orderApi;

    @Before
    public void setUp(){
        order1 = createObject(2L, "abc", 1L, 12L, OrderStatus.CREATED);
        order2 = createObject(4L, "pqr", 3L, 9L, OrderStatus.CREATED);
    }

    public Order createObject(Long clientId, String channelOrderId, Long channelId, Long customerId, OrderStatus status) {
        Order order = new Order();
        order.setClientId(clientId);
        order.setChannelOrderId(channelOrderId);
        order.setChannelId(channelId);
        order.setCustomerId(customerId);
        order.setStatus(status);
        return order;
    }

    // test for add order
    @Test
    public void testAddOrder(){
        orderApi.addOrder(order1);
        Order newOrder = orderApi.get(order1.getId());
        assertNotNull(newOrder);
        assertTrue(newOrder.getId()>0);
        // test data
        assertEquals(order1.getClientId(), newOrder.getClientId());
        assertEquals(order1.getChannelId(), newOrder.getChannelId());
        assertEquals(order1.getChannelOrderId(), newOrder.getChannelOrderId());
        assertEquals(order1.getCustomerId(), newOrder.getCustomerId());
        assertEquals(order1.getStatus(), newOrder.getStatus());
    }

    // test for update order
    @Test
    public void testUpdateOrder() {
        orderApi.addOrder(order2);
        Order newOrder = orderApi.get(order2.getId());
        assertNotNull(newOrder);
        // update status
        newOrder.setStatus(OrderStatus.ALLOCATED);
        orderApi.updateOrder(newOrder);
        Order updatedOrder = orderApi.get(order2.getId());
        assertNotNull(updatedOrder);
        // test data
        assertEquals(newOrder.getStatus(), updatedOrder.getStatus());
    }

    // test for getting order by id
    @Test
    public void testGet() {
        orderApi.addOrder(order1);
        Order newOrder = orderApi.get(order1.getId());
        assertNotNull(newOrder);
        assertTrue(newOrder.getId()>0);
        // test data
        assertEquals(order1.getClientId(), newOrder.getClientId());
        assertEquals(order1.getChannelId(), newOrder.getChannelId());
        assertEquals(order1.getChannelOrderId(), newOrder.getChannelOrderId());
        assertEquals(order1.getCustomerId(), newOrder.getCustomerId());
        assertEquals(order1.getStatus(), newOrder.getStatus());
    }

    // test for get all
    @Test
    public void testGetAll(){
        orderApi.addOrder(order1);
        orderApi.addOrder(order2);
        List<Order> orderList = orderApi.getAll();
        // list size 2
        assertEquals(2,orderList.size());
    }

    // test for getting orders by channelOrderId and channelId
    @Test
    public void testGetByChannelDetails(){
        orderApi.addOrder(order1);
        orderApi.addOrder(order2);
        Order order = orderApi.getOrderByChannelOrderIdAndChannelId(order1.getChannelOrderId(),order1.getChannelId());
        // not null
        assertNotNull(order);
        order =orderApi.getOrderByChannelOrderIdAndChannelId(order1.getChannelOrderId(),order2.getChannelId());
        // null
        assertNull(order);
    }

    // test for getting by status
    @Test
    public void testGetByStatus(){
        orderApi.addOrder(order1);
        orderApi.addOrder(order2);
        List<Order> orderList = orderApi.getOrdersByStatus(OrderStatus.CREATED);
        // 2 created orders
        assertEquals(2,orderList.size());
        orderList = orderApi.getOrdersByStatus(OrderStatus.ALLOCATED);
        // 0 allocated orders
        assertEquals(0,orderList.size());
    }

    // test for getCheck
    @Test(expected = ApiException.class)
    public void testGetCheck(){
        orderApi.addOrder(order1);
        Order newOrder = orderApi.getCheck(order1.getId());
        assertNotNull(newOrder);
        assertTrue(newOrder.getId()>0);
        assertEquals(order1.getClientId(), newOrder.getClientId());
        assertEquals(order1.getChannelId(), newOrder.getChannelId());
        assertEquals(order1.getChannelOrderId(), newOrder.getChannelOrderId());
        assertEquals(order1.getCustomerId(), newOrder.getCustomerId());
        assertEquals(order1.getStatus(), newOrder.getStatus());
        // throw exception
        orderApi.getCheck(order1.getId()+1);
    }

}
