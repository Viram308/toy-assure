package com.channel.assure;


import com.commons.response.OrderItemData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderItemAssure extends AbstractRestTemplate {
    @Value("${server.url}")
    private String SERVER_URL;

    public List<OrderItemData> getOrderItems(Long orderId) {
        String POST_ORDER_URL = SERVER_URL + "/order/items/" + orderId;
        HttpEntity<OrderItemData[]> entity = new HttpEntity<>(getHeaders());
        return Arrays.asList(restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderItemData[].class).getBody());
    }
}
