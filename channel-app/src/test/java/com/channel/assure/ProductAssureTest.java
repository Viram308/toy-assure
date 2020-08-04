package com.channel.assure;

import com.channel.spring.AbstractUnitTest;
import com.commons.response.ProductData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
public class ProductAssureTest extends AbstractUnitTest {

    List<ProductData> productResponseList;
    ProductData product1, product2;

    @Autowired
    private ProductAssure productAssure;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        productResponseList = new ArrayList<>();
        product1 = createObject("client11", "abc", "prod1", "brand1", 19.9, "desc1");
        productResponseList.add(product1);
        product2 = createObject("client12", "pqr", "prod2", "brand2", 29.9, "desc2");
        productResponseList.add(product2);
    }

    public ProductData createObject(String clientName, String clientSkuId, String name,
                                    String brandId, Double mrp, String description) {
        ProductData product = new ProductData();
        product.setClientName(clientName);
        product.setClientSkuId(clientSkuId);
        product.setProductName(name);
        product.setBrandId(brandId);
        product.setMrp(mrp);
        product.setDescription(description);
        return product;
    }

    @Test
    public void testGetProductDetails() {
        long globalSkuId = 1L;

        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL + "/product/" + globalSkuId)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(productResponseList.get(0)))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        ProductData productData = productAssure.getProductData(globalSkuId);
        mockServer.verify();
        assertNotNull(productData);

        assertEquals(product1.getClientName(), productData.getClientName());
        assertEquals(product1.getClientSkuId(), productData.getClientSkuId());
        assertEquals(product1.getProductName(), productData.getProductName());
        assertEquals(product1.getBrandId(), productData.getBrandId());
        assertEquals(product1.getMrp(), productData.getMrp(), 0.01);
        assertEquals(product1.getDescription(), productData.getDescription());
    }
}
