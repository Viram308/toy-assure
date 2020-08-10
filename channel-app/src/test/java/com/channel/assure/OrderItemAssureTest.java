package com.channel.assure;

import com.channel.spring.AbstractUnitTest;
import com.commons.response.OrderItemData;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class OrderItemAssureTest extends AbstractUnitTest {

    List<OrderItemData> orderItemResponseList;
    OrderItemData orderItem1;
    OrderItemData orderItem2;

    @Autowired
    private OrderItemAssure orderItemAssure;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        orderItemResponseList = new ArrayList<>();
        orderItem1 = createObject(5L, "nestle","prod1","munch",10.60);
        orderItemResponseList.add(orderItem1);
        orderItem2 = createObject(15L, "nestle","prod2","kitkat",10.80);
        orderItemResponseList.add(orderItem2);
    }

    public OrderItemData createObject(Long orderedQuantity,String brandId,String clientSkuId,String productName,double sellingPricePerUnit) {
        OrderItemData response = new OrderItemData();
        response.setOrderedQuantity(orderedQuantity);
        response.setBrandId(brandId);
        response.setClientSkuId(clientSkuId);
        response.setProductName(productName);
        response.setSellingPricePerUnit(sellingPricePerUnit);
        return response;
    }

    // test for getting line items
    @Test
    public void testGetOrderItemDetails() {
        Long orderId = 10L;

        try {
            // create mock server
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/order/items/"+orderId)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(orderItemResponseList))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        List<OrderItemData> responseList = orderItemAssure.getOrderItems(orderId);
        mockServer.verify();
        Assert.assertNotNull(responseList);
        Assert.assertTrue(responseList.size()>0);
        // test each line item
        assertEquals(orderItem1.getBrandId(), responseList.get(0).getBrandId());
        assertEquals(orderItem1.getOrderedQuantity(), responseList.get(0).getOrderedQuantity());
        assertEquals(orderItem1.getClientSkuId(),responseList.get(0).getClientSkuId());
        assertEquals(orderItem1.getProductName(),responseList.get(0).getProductName());
        assertEquals(orderItem1.getSellingPricePerUnit(),responseList.get(0).getSellingPricePerUnit(),0.01);


        assertEquals(orderItem2.getBrandId(), responseList.get(1).getBrandId());
        assertEquals(orderItem2.getOrderedQuantity(), responseList.get(1).getOrderedQuantity());
        assertEquals(orderItem2.getClientSkuId(),responseList.get(1).getClientSkuId());
        assertEquals(orderItem2.getProductName(),responseList.get(1).getProductName());
        assertEquals(orderItem2.getSellingPricePerUnit(),responseList.get(1).getSellingPricePerUnit(),0.01);
    }
}
