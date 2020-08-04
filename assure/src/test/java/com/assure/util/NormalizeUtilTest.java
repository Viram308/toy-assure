package com.assure.util;

import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.assure.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalizeUtilTest extends AbstractUnitTest {
    private Client client;
    private Product product;

    @Before
    public void setUp() {
        client = createClient();
        product = createProduct();
    }

    private Product createProduct() {
        Product product = new Product();
        product.setName("muNch");
        product.setClientId(1L);
        product.setBrandId("Nestle    ");
        product.setClientSkuId("Prod1");
        product.setMrp(20.50);
        product.setDescription(" nIce ");
        return product;
    }

    private Client createClient() {
        Client client = new Client();
        client.setName("testClient");
        client.setType(ClientType.CLIENT);
        return client;
    }

    @Test
    public void testNormalizeClient(){
        NormalizeUtil.normalizeClient(client);
        assertEquals("testclient",client.getName());
        assertEquals(ClientType.CLIENT,client.getType());
    }

    @Test
    public void testNormalizeProduct(){
        NormalizeUtil.normalizeProduct(product);
        assertEquals("prod1",product.getClientSkuId());
        assertEquals("munch",product.getName());
        assertEquals("nice",product.getDescription());
        assertEquals("nestle",product.getBrandId());
    }

}
