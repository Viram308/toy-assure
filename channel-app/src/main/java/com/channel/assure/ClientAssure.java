package com.channel.assure;

import com.commons.response.ClientData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ClientAssure extends AbstractRestTemplate {
    @Value("${server.url}")
    private String SERVER_URL;

    public List<ClientData> getClientDetails() {
        String GET_CLIENT_URL = SERVER_URL + "/client/allClients";
        ClientData[] response;
        HttpEntity<ClientData[]> entity = new HttpEntity<>(getHeaders());
        response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientData[].class).getBody();
        return Arrays.asList(response);
    }

    public ClientData getClientData(Long clientId) {
        String GET_CLIENT_URL = SERVER_URL + "/client/" + clientId;
        ClientData response;
        HttpEntity<ClientData> entity = new HttpEntity<>(getHeaders());
        response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientData.class).getBody();
        return response;

    }

    public List<ClientData> getCustomerDetails() {
        String GET_CLIENT_URL = SERVER_URL + "/client/allCustomers";
        ClientData[] response;
        HttpEntity<ClientData[]> entity = new HttpEntity<>(getHeaders());
        response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientData[].class).getBody();
        return Arrays.asList(response);

    }
}
