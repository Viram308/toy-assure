package com.assure.dao;

import com.assure.pojo.Order;
import com.assure.pojo.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    // select By orderId
    private static String selectByOrderId = "select o from OrderItem o where o.orderId=:orderId";

    public List<OrderItem> selectByOrderId(Long orderId) {
        TypedQuery<OrderItem> query = getQuery(selectByOrderId, OrderItem.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
