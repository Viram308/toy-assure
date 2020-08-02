package com.channel.assure;

import com.commons.form.OrderCsvForm;
import com.commons.form.OrderSearchForm;
import com.commons.response.OrderData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderAssure extends AbstractRestTemplate {

    @Value("${server.url}")
    private String SERVER_URL;

    public String addChannelOrder(OrderCsvForm orderCsvForm) {
        String POST_ORDER_URL = SERVER_URL + "/order/addChannelOrder";
        try{
            HttpEntity<OrderCsvForm> requestBody = new HttpEntity<>(orderCsvForm, getHeaders());
            return restTemplate.exchange(POST_ORDER_URL, HttpMethod.POST, requestBody,String.class).getBody();
        }catch (HttpStatusCodeException e) {
            e.getMessage();
        }
        return "Error";
    }

    public List<OrderData> getChannelOrders() {
        String POST_ORDER_URL = SERVER_URL + "/order";
        try{
            HttpEntity<OrderData[]> entity = new HttpEntity<>(getHeaders());
            return Arrays.asList(restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderData[].class).getBody());
        }catch (HttpStatusCodeException e) {
            e.getMessage();
        }
        return null;
    }

    public List<OrderData> searchChannelOrder(OrderSearchForm orderSearchForm) {
        String POST_ORDER_URL = SERVER_URL + "/order/searchChannelOrder";
        try{
            HttpEntity<OrderSearchForm> requestBody = new HttpEntity<>(orderSearchForm,getHeaders());
            return Arrays.asList(restTemplate.exchange(POST_ORDER_URL, HttpMethod.POST, requestBody, OrderData[].class).getBody());
        }catch (HttpStatusCodeException e) {
            e.getMessage();
        }
        return null;
    }

    public OrderData get(Long id) {
        String POST_ORDER_URL = SERVER_URL + "/order/"+id;
        try{
            HttpEntity<OrderData> entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderData.class).getBody();
        }catch (HttpStatusCodeException e) {
            e.getMessage();
        }
        return null;
    }

    public OrderData getOrderDetails(String channelOrderId, Long channelId) {
        String POST_ORDER_URL = SERVER_URL + "/order/"+channelId + "/"+channelOrderId;
        try{
            HttpEntity<OrderData> entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(POST_ORDER_URL, HttpMethod.GET, entity, OrderData.class).getBody();
        }catch (HttpStatusCodeException e) {
            e.getMessage();
        }
        return null;
    }
}
