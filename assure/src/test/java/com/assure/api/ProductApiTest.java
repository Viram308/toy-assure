package com.assure.api;

import com.assure.pojo.Product;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProductApiTest extends AbstractUnitTest {

    private Product product1, product2, product3;
    @Autowired
    private ProductApi productApi;

    @Before
    public void setUp() {
        product1 = createObject("prod1", 1L, "brand1", "sku1", 19.0, "text1");
        product2 = createObject("prod2", 2L, "brand2", "sku2", 29.0, "text2");
        product3 = createObject("prod3", 2L, "brand3", "sku3", 39.0, "text3");
    }

    public Product createObject(String name, Long clientId, String brandId, String clientSkuId, Double mrp, String description) {
        Product product = new Product();
        product.setName(name);
        product.setClientId(clientId);
        product.setBrandId(brandId);
        product.setClientSkuId(clientSkuId);
        product.setMrp(mrp);
        product.setDescription(description);
        return product;
    }

    @Test
    public void testAdd() {
        Product product = productApi.add(product1);
        assertNotNull(product);
        assertEquals(product1.getClientId(), product.getClientId());
        assertEquals(product1.getBrandId(), product.getBrandId());
        assertEquals(product1.getName(), product.getName());
        assertEquals(product1.getClientSkuId(), product.getClientSkuId());
        assertEquals(product1.getDescription(), product.getDescription());
        assertEquals(product1.getMrp(), product.getMrp(), 0.01);
    }

    @Test
    public void testGet() {
        Product newProduct1 = productApi.add(product1);
        assertNotNull(newProduct1);
        Product product = productApi.get(newProduct1.getGlobalSkuId());
        assertNotNull(product);
        assertEquals(product1.getClientId(), product.getClientId());
        assertEquals(product1.getBrandId(), product.getBrandId());
        assertEquals(product1.getName(), product.getName());
        assertEquals(product1.getClientSkuId(), product.getClientSkuId());
        assertEquals(product1.getDescription(), product.getDescription());
        assertEquals(product1.getMrp(), product.getMrp(), 0.01);
    }

    @Test
    public void testSearch() {
        productApi.add(product1);
        productApi.add(product2);
        productApi.add(product3);
        List<Product> productList = productApi.search("p");
        assertEquals(3, productList.size());
        productList = productApi.search("prod2");
        assertEquals(1, productList.size());
    }

    @Test
    public void testUpdate() {
        productApi.add(product1);
        Product product = new Product();
        product.setName("New ProducT 2");
        product.setBrandId("New BrandId                 ");
        product = productApi.update(product1.getGlobalSkuId(), product);
        assertNotNull(product);
        assertEquals("new brandid", product.getBrandId());
        assertEquals("new product 2", product.getName());
    }

    @Test
    public void testGetAll() {
        productApi.add(product1);
        productApi.add(product2);
        List<Product> list = productApi.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test(expected = ApiException.class)
    public void testGetCheck(){
        productApi.add(product1);
        productApi.add(product2);
        Product product = productApi.getCheck(product1.getGlobalSkuId());
        assertNotNull(product);
        assertEquals(product1.getClientId(), product.getClientId());
        assertEquals(product1.getBrandId(), product.getBrandId());
        assertEquals(product1.getName(), product.getName());
        assertEquals(product1.getClientSkuId(), product.getClientSkuId());
        assertEquals(product1.getDescription(), product.getDescription());
        assertEquals(product1.getMrp(), product.getMrp(), 0.01);
        // throw exception
        productApi.getCheck(product2.getGlobalSkuId()+1);
    }

    @Test(expected = ApiException.class)
    public void testGetCheckExisting(){
        productApi.add(product1);
        productApi.add(product2);
        productApi.getCheckExisting("a",1L);
        // throw exception if already exist
        productApi.getCheckExisting(product1.getClientSkuId(),product1.getClientId());
    }

    @Test
    public void testGetProductByClientIdAndClientSkuId(){
        productApi.add(product1);
        Product product = productApi.getByClientIdAndClientSkuId(product1.getClientId(),product1.getClientSkuId());
        assertNotNull(product);
        assertEquals(product1.getClientId(), product.getClientId());
        assertEquals(product1.getBrandId(), product.getBrandId());
        assertEquals(product1.getName(), product.getName());
        assertEquals(product1.getClientSkuId(), product.getClientSkuId());
        assertEquals(product1.getDescription(), product.getDescription());
        assertEquals(product1.getMrp(), product.getMrp(), 0.01);
        product = productApi.getByClientIdAndClientSkuId(3L,"hi");
        assertNull(product);
    }

    @Test
    public void testGetByClientId(){
        productApi.add(product1);
        productApi.add(product2);
        productApi.add(product3);
        List<Product> productList = productApi.getByClientId(1L);
        assertEquals(1,productList.size());
        productList = productApi.getByClientId(2L);
        assertEquals(2,productList.size());
    }

}
