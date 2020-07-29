package com.channel.assure;

import com.commons.response.ClientData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

@Component
public class ClientAssure extends AbstractRestTemplate {
    @Value("${server.url}")
    private String SERVER_URL;

    public List<ClientData> getClientDetails() {
        String GET_CLIENT_URL = SERVER_URL + "/client/allClients";
        try {
            ClientData[] response;
            HttpEntity<ClientData[]> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientData[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public ClientData getClientData(Long clientId) {
        String GET_CLIENT_URL = SERVER_URL + "/client/"+clientId;
        try {
            ClientData response;
            HttpEntity<ClientData> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientData.class).getBody();
            return response;
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public List<ClientData> getCustomerDetails() {
        String GET_CLIENT_URL = SERVER_URL + "/client/allCustomers";
        try {
            ClientData[] response;
            HttpEntity<ClientData[]> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientData[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
