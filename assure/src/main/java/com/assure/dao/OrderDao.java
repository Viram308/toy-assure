package com.assure.dao;

import com.assure.pojo.Order;
import com.commons.enums.OrderStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{

    // select according to channelOrderId and channelId
    private static String selectByChannelOrderIdChannelId = "select o from Order o where o.channelOrderId=:channelOrderId and p.channelId=:channelId";
    // select All
    private static String selectAll = "select o from Order o";
    // select By status
    private static String selectByStatus = "select o from Order o where o.status=:status";

    public Order selectByChannelOrderIdAndChannelId(String channelOrderId, Long channelId) {
        TypedQuery<Order> query = getQuery(selectByChannelOrderIdChannelId, Order.class);
        query.setParameter("channelOrderId", channelOrderId);
        query.setParameter("channelId", channelId);
        return getSingle(query);
    }

    public List<Order> selectAll() {
        TypedQuery<Order> query = getQuery(selectAll, Order.class);
        return query.getResultList();
    }

    public List<Order> selectByStatus(OrderStatus status) {
        TypedQuery<Order> query = getQuery(selectByStatus, Order.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
