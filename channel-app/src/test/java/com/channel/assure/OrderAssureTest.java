package com.channel.assure;

import com.channel.spring.AbstractUnitTest;
import com.commons.enums.OrderStatus;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderSearchForm;
import com.commons.response.OrderData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class OrderAssureTest extends AbstractUnitTest {

    private final List<OrderData> orderResponseList = new ArrayList<>();
    private final OrderCsvForm orderCsvForm = new OrderCsvForm();
    private final OrderSearchForm orderSearchForm = new OrderSearchForm();
    OrderData order1, order2;

    @Autowired
    private OrderAssure orderAssure;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        order1 = createObject(1L, 2L, "customer1",
                "channelOrder1", OrderStatus.CREATED, "channel1");

        order2 = createObject(4L, 5L, "customer2",
                "channelOrder2", OrderStatus.CREATED, "channel2");
    }

    public OrderData createObject(Long clientId, Long channelId,
                                          String customerName, String channelOrderId,
                                          OrderStatus status, String channelName) {
        OrderData OrderData = new OrderData();
        OrderData.setClientId(clientId);
        OrderData.setChannelId(channelId);
        OrderData.setCustomerName(customerName);
        OrderData.setChannelOrderId(channelOrderId);
        OrderData.setStatus(status.toString());
        OrderData.setChannelName(channelName);
        return OrderData;
    }

    @Test
    public void testGetOrderDetails() {
        orderResponseList.add(order1);
        orderResponseList.add(order2);
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/order")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(orderResponseList))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        List<OrderData> responseList = orderAssure.getChannelOrders();
        mockServer.verify();
        Assert.assertNotNull(responseList);
        Assert.assertTrue(responseList.size()>0);

        assertEquals(order1.getClientId(), responseList.get(0).getClientId());
        assertEquals(order1.getChannelId(), responseList.get(0).getChannelId());
        assertEquals(order1.getCustomerName(), responseList.get(0).getCustomerName());
        assertEquals(order1.getChannelOrderId(), responseList.get(0).getChannelOrderId());
        assertEquals(order1.getStatus(), responseList.get(0).getStatus());
        assertEquals(order1.getClientName(), responseList.get(0).getClientName());

        assertEquals(order2.getClientId(), responseList.get(1).getClientId());
        assertEquals(order2.getChannelId(), responseList.get(1).getChannelId());
        assertEquals(order2.getCustomerName(), responseList.get(1).getCustomerName());
        assertEquals(order2.getChannelOrderId(), responseList.get(1).getChannelOrderId());
        assertEquals(order2.getStatus(), responseList.get(1).getStatus());
        assertEquals(order2.getClientName(), responseList.get(1).getClientName());
    }

    @Test
    public void testAddChannelOrder(){
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/order/addChannelOrder")))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withStatus(HttpStatus.OK)
                            .body("Success")
                    );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String response = orderAssure.addChannelOrder(orderCsvForm);
        assertEquals("Success",response);
    }

    @Test
    public void testSearchChannelOrder(){
        orderResponseList.add(order1);
        orderResponseList.add(order2);
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/order/searchChannelOrder")))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(orderResponseList))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        List<OrderData> responseList = orderAssure.searchChannelOrder(orderSearchForm);
        mockServer.verify();
        Assert.assertNotNull(responseList);
        Assert.assertTrue(responseList.size()>0);

        assertEquals(order1.getClientId(), responseList.get(0).getClientId());
        assertEquals(order1.getChannelId(), responseList.get(0).getChannelId());
        assertEquals(order1.getCustomerName(), responseList.get(0).getCustomerName());
        assertEquals(order1.getChannelOrderId(), responseList.get(0).getChannelOrderId());
        assertEquals(order1.getStatus(), responseList.get(0).getStatus());
        assertEquals(order1.getClientName(), responseList.get(0).getClientName());

        assertEquals(order2.getClientId(), responseList.get(1).getClientId());
        assertEquals(order2.getChannelId(), responseList.get(1).getChannelId());
        assertEquals(order2.getCustomerName(), responseList.get(1).getCustomerName());
        assertEquals(order2.getChannelOrderId(), responseList.get(1).getChannelOrderId());
        assertEquals(order2.getStatus(), responseList.get(1).getStatus());
        assertEquals(order2.getClientName(), responseList.get(1).getClientName());
    }

    @Test
    public void testGet(){
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/order/"+order1.getOrderId())))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(order1))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        OrderData orderData = orderAssure.get(order1.getOrderId());
        assertNotNull(orderData);
        assertEquals(order1.getClientId(), orderData.getClientId());
        assertEquals(order1.getChannelId(), orderData.getChannelId());
        assertEquals(order1.getCustomerName(), orderData.getCustomerName());
        assertEquals(order1.getChannelOrderId(), orderData.getChannelOrderId());
        assertEquals(order1.getStatus(), orderData.getStatus());
        assertEquals(order1.getClientName(), orderData.getClientName());
    }

    @Test
    public void testGetOrderDetailsByParameters(){
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL + "/order/" + order1.getChannelId() + "/" + order1.getChannelOrderId())))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(order1))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        OrderData orderData = orderAssure.getOrderDetails(order1.getChannelOrderId(),order1.getChannelId());
        assertNotNull(orderData);
        assertEquals(order1.getClientId(), orderData.getClientId());
        assertEquals(order1.getChannelId(), orderData.getChannelId());
        assertEquals(order1.getCustomerName(), orderData.getCustomerName());
        assertEquals(order1.getChannelOrderId(), orderData.getChannelOrderId());
        assertEquals(order1.getStatus(), orderData.getStatus());
        assertEquals(order1.getClientName(), orderData.getClientName());
    }

}
