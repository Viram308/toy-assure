package com.assure.api;

import com.assure.dao.OrderDao;
import com.assure.pojo.Order;
import com.commons.api.ApiException;
import com.commons.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    @Transactional
    public void addOrder(Order order) {
        orderDao.insert(order);
    }

    @Transactional(readOnly = true)
    public Order get(Long id) {
        return orderDao.select(Order.class,id);
    }

    @Transactional(readOnly = true)
    public Order getOrderByChannelOrderIdAndChannelId(String channelOrderId, Long channelId) {
        return orderDao.selectByChannelOrderIdAndChannelId(channelOrderId,channelId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderDao.selectAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderDao.selectByStatus(status);
    }

    @Transactional
    public void updateOrder(Order order) {
        Order orderUpdate = getCheck(order.getId());
        orderUpdate.setStatus(order.getStatus());
        orderDao.update(orderUpdate);
    }

    @Transactional(readOnly = true)
    public Order getCheck(Long id) {
        Order order = orderDao.select(Order.class,id);
        if(order == null){
            throw new ApiException("Order doesn't exist for orderId : "+id);
        }
        return order;
    }
}
