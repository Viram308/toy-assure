package com.channel.assure;

import com.channel.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.response.ClientData;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
public class ClientAssureTest extends AbstractUnitTest {

    List<ClientData> clientResponseList;
    ClientData client1;

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
        clientResponseList.add(client1);

    }

    public ClientData createObject(String name, ClientType type, Long id){
        ClientData ClientData = new ClientData();
        ClientData.setName(name);
        ClientData.setType(type.toString());
        ClientData.setId(id);
        return ClientData;
    }

    @Test
    public void testGetChannelDetails() {

        List<ClientData> responseList = null;
        try {
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
        assertEquals(client1.getId(), responseList.get(0).getId());
        assertEquals(client1.getName(), responseList.get(0).getName());
        assertEquals(client1.getType(), responseList.get(0).getType());
    }
}
