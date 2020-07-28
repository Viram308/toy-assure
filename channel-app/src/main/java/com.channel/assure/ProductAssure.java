package com.channel.assure;

import com.commons.form.ProductSearchForm;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

public class ProductAssure extends AbstractRestTemplate {

    @Value("${server.url}")
    private String SERVER_URL;

    public List<ProductData> getProductByClientIdAndClientSkuId(Long clientId) {
        ProductSearchForm productSearchForm = new ProductSearchForm();
        productSearchForm.setClientId(clientId);
        productSearchForm.setProductName("");
        String GET_PRODUCT_URL = SERVER_URL + "/product/search";
        try {
            ProductData[] response;
            HttpEntity<ProductSearchForm> requestBody = new HttpEntity<>(productSearchForm,getHeaders());
            response = restTemplate.exchange(GET_PRODUCT_URL, HttpMethod.POST, requestBody, ProductData[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public ProductData getProductData(Long globalSkuId) {
        String GET_PRODUCT_URL = SERVER_URL + "/product/"+ globalSkuId;
        try {
            ProductData response;
            HttpEntity<ProductData> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_PRODUCT_URL, HttpMethod.GET, entity, ProductData.class).getBody();
            return response;
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

}
