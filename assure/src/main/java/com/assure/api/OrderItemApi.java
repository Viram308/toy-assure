package com.assure.api;

import com.assure.dao.OrderItemDao;
import com.assure.pojo.OrderItem;
import com.assure.util.NormalizeUtil;
import com.commons.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemApi {

    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional
    public OrderItem addOrderItem(OrderItem orderItem) {
        NormalizeUtil.normalizeOrderItem(orderItem);
        return orderItemDao.insert(orderItem);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItemByOrderId(Long orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateOrderItem(Long orderItemId,OrderItem orderItem) {
        NormalizeUtil.normalizeOrderItem(orderItem);
        OrderItem orderItemUpdate = getCheck(orderItemId);
        orderItemUpdate.setAllocatedQuantity(orderItem.getAllocatedQuantity());
        orderItemUpdate.setGlobalSkuId(orderItem.getGlobalSkuId());
        orderItemUpdate.setFulfilledQuantity(orderItem.getFulfilledQuantity());
        orderItemUpdate.setOrderedQuantity(orderItem.getOrderedQuantity());
        orderItemUpdate.setOrderId(orderItem.getOrderId());
        orderItemUpdate.setSellingPricePerUnit(orderItem.getSellingPricePerUnit());
        orderItemDao.update(orderItemUpdate);
    }

    @Transactional(readOnly = true)
    public OrderItem getCheck(Long orderItemId) {
        OrderItem orderItem = orderItemDao.select(OrderItem.class,orderItemId);
        if(orderItem == null){
            throw new ApiException("Order doesn't exist for orderItemId : "+orderItemId);
        }
        return orderItem;
    }


}
