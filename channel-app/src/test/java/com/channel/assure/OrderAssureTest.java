package com.channel.assure;

import com.channel.spring.AbstractUnitTest;
import com.commons.enums.OrderStatus;
import com.commons.response.OrderData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
public class OrderAssureTest extends AbstractUnitTest {

    List<OrderData> orderResponseList;
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
        orderResponseList = new ArrayList<>();
        order1 = createObject(1L, 2L, "customer1",
                "channelOrder1", OrderStatus.CREATED, "channel1");
        orderResponseList.add(order1);
        order2 = createObject(4L, 5L, "customer2",
                "channelOrder2", OrderStatus.CREATED, "channel2");
        orderResponseList.add(order2);
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

        Assert.assertEquals(order1.getClientId(), responseList.get(0).getClientId());
        Assert.assertEquals(order1.getChannelId(), responseList.get(0).getChannelId());
        Assert.assertEquals(order1.getCustomerName(), responseList.get(0).getCustomerName());
        Assert.assertEquals(order1.getChannelOrderId(), responseList.get(0).getChannelOrderId());
        Assert.assertEquals(order1.getStatus(), responseList.get(0).getStatus());
        Assert.assertEquals(order1.getClientName(), responseList.get(0).getClientName());

        Assert.assertEquals(order2.getClientId(), responseList.get(1).getClientId());
        Assert.assertEquals(order2.getChannelId(), responseList.get(1).getChannelId());
        Assert.assertEquals(order2.getCustomerName(), responseList.get(1).getCustomerName());
        Assert.assertEquals(order2.getChannelOrderId(), responseList.get(1).getChannelOrderId());
        Assert.assertEquals(order2.getStatus(), responseList.get(1).getStatus());
        Assert.assertEquals(order2.getClientName(), responseList.get(1).getClientName());
    }
}
