package com.assure.channel;

import com.assure.spring.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import com.commons.response.ChannelData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class ChannelDataApiTest extends AbstractUnitTest {
    @Autowired
    private ChannelDataApi channelDataApi;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    // test for getting channel data from server
    @Test
    public void testGetChannelDetails() {
        Long channelId = 1L;

        ChannelData channelDataResponse = new ChannelData();
        channelDataResponse.setName("test");
        channelDataResponse.setInvoiceType(InvoiceType.CHANNEL.toString());
        channelDataResponse.setId(1L);
        // create mockServer
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI("http://localhost:9003/oms/api/channel/1")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(channelDataResponse))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        ChannelData response = channelDataApi.getChannelDetails(channelId);
        mockServer.verify();
        // test data
        assertEquals(channelDataResponse.getId(), response.getId());
        assertEquals(channelDataResponse.getName(), response.getName());
        assertEquals(channelDataResponse.getInvoiceType(), response.getInvoiceType());
    }

    // test for getting all channel from server
    @Test
    public void testGetAllChannel(){
        List<ChannelData> channelDataList = new ArrayList<>();
        ChannelData channelData = new ChannelData();
        channelData.setName("test");
        channelData.setInvoiceType(InvoiceType.CHANNEL.toString());
        channelData.setId(1L);
        channelDataList.add(channelData);
        // create mockServer
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI("http://localhost:9003/oms/api/channel")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(channelDataList))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        List<ChannelData>  response = channelDataApi.getAllChannel();
        mockServer.verify();
        // test list size
        assertEquals(1,response.size());

    }

    // test for getting pdfBytes from channel-app
    @Test
    public void testGetPdfBytes(){
        byte[] bytes = new byte[30];
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI("http://localhost:9003/oms/api/channelOrder/download/" + 1)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(bytes))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        byte[]  response = channelDataApi.getPDFBytes(1L);
        mockServer.verify();
        assertTrue(response.length > 0);
    }



}
