package com.channel.assure;

import com.commons.form.OrderCsvForm;
import com.commons.form.OrderSearchForm;
import com.commons.response.ChannelData;
import com.commons.response.OrderData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderAssure extends AbstractRestTemplate {

    @Value("${server.url}")
    private String SERVER_URL;

    public String addChannelOrder(OrderCsvForm orderCsvForm) {
        String POST_ORDER_URL = SERVER_URL + "/order/addChannelOrder";
        HttpEntity<OrderCsvForm> requestBody = new HttpEntity<>(orderCsvForm, getHeaders());
        return restTemplate.exchange(POST_ORDER_URL, HttpMethod.POST, requestBody, String.class).getBody();

    }

    public List<OrderData> getChannelOrders() {
        String POST_ORDER_URL = SERVER_URL + "/order";
        HttpEntity<OrderData[]> entity = new HttpEntity<>(getHeaders());
        return Arrays.asList(restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderData[].class).getBody());
    }

    public List<OrderData> searchChannelOrder(OrderSearchForm orderSearchForm) {
        String POST_ORDER_URL = SERVER_URL + "/order/searchChannelOrder";
        HttpEntity<OrderSearchForm> requestBody = new HttpEntity<>(orderSearchForm, getHeaders());
        return Arrays.asList(restTemplate.exchange(POST_ORDER_URL, HttpMethod.POST, requestBody, OrderData[].class).getBody());
    }

    public OrderData get(Long id) {
        String POST_ORDER_URL = SERVER_URL + "/order/" + id;
        HttpEntity<OrderData> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderData.class).getBody();
    }

    public OrderData getOrderDetails(String channelOrderId, Long channelId) {
        String POST_ORDER_URL = SERVER_URL + "/order/" + channelId + "/" + channelOrderId;
        HttpEntity<OrderData> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderData.class).getBody();

    }

    public byte[] getPDFBytes(Long id) {
        String GET_CHANNEL_URL = SERVER_URL + "/order/download/" + id;

        HttpEntity<ChannelData> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(GET_CHANNEL_URL, HttpMethod.GET, entity, byte[].class, "1").getBody();
    }
}
