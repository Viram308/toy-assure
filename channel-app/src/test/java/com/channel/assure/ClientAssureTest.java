package com.channel.assure;

import com.channel.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.response.ClientData;
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

public class ClientAssureTest extends AbstractUnitTest {

    List<ClientData> clientResponseList;
    ClientData client1,customer;

    @Autowired
    private ClientAssure clientAssure;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        clientResponseList = new ArrayList<>();

        client1 = createObject("cli1", ClientType.CLIENT, 1L);
        customer = createObject("cli2",ClientType.CUSTOMER,2L);

    }

    private ClientData createObject(String name, ClientType type, Long id){
        ClientData ClientData = new ClientData();
        ClientData.setName(name);
        ClientData.setType(type.toString());
        ClientData.setId(id);
        return ClientData;
    }

    // test for getting client details
    @Test
    public void testGetClientDetails() {

        List<ClientData> responseList = null;
        clientResponseList.add(client1);
        try {
            // create mock server
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL + "/client/allClients")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(clientResponseList))
                    );
            responseList = clientAssure.getClientDetails();

        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        mockServer.verify();
        Assert.assertNotNull(responseList);
        assertEquals(1,responseList.size());
        // test data
        assertEquals(client1.getId(), responseList.get(0).getId());
        assertEquals(client1.getName(), responseList.get(0).getName());
        assertEquals(client1.getType(), responseList.get(0).getType());
    }

    // test for getting customer details
    @Test
    public void testGetCustomerDetails() {

        List<ClientData> responseList = null;
        clientResponseList.add(customer);
        try {
            // create mock server
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL + "/client/allCustomers")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(clientResponseList))
                    );
            responseList = clientAssure.getCustomerDetails();

        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        mockServer.verify();
        Assert.assertNotNull(responseList);
        // test data
        assertEquals(1,responseList.size());
        assertEquals(customer.getId(), responseList.get(0).getId());
        assertEquals(customer.getName(), responseList.get(0).getName());
        assertEquals(customer.getType(), responseList.get(0).getType());
    }

    // test for getting client by id
    @Test
    public void testGetClientDetailsById() {

        ClientData clientData = new ClientData();

        try {
            // create mock server
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL + "/client/"+client1.getId())))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(client1))
                    );
            clientData = clientAssure.getClientData(client1.getId());

        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        mockServer.verify();
        Assert.assertNotNull(clientData);
        // test data
        assertEquals(client1.getId(), clientData.getId());
        assertEquals(client1.getName(), clientData.getName());
        assertEquals(client1.getType(), clientData.getType());
    }

}
