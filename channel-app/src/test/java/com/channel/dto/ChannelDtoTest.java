package com.channel.dto;

import com.channel.api.ChannelListingApi;
import com.channel.assure.ClientAssure;
import com.channel.model.form.ChannelForm;
import com.channel.pojo.ChannelListing;
import com.channel.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.enums.InvoiceType;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ChannelDtoTest extends AbstractUnitTest {
    private ChannelForm channelForm1,channelForm2;
    private ClientData clientData1,clientData2;
    private ChannelListing channelListing1;

    @Autowired
    private ChannelDto channelDto;
    @Mock
    private ChannelListingApi channelListingApi;
    @Mock
    private ClientAssure clientAssure;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        channelForm1 = createChannelForm("chanNel1","channel");
        channelForm2 = createChannelForm("chaN","");
        clientData1 = createClientData(1L,"viram",ClientType.CLIENT.toString());
        clientData2 = createClientData(2L,"viram308",ClientType.CUSTOMER.toString());
        channelListing1 = createChannelListing();
    }

    private ChannelListing createChannelListing() {
        return new ChannelListing();
    }

    private ClientData createClientData(Long clientId,String clientName,String type) {
        ClientData clientData = new ClientData();
        clientData.setId(clientId);
        clientData.setName(clientName);
        clientData.setType(type);
        return clientData;
    }

    private ChannelForm createChannelForm(String name,String type) {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName(name);
        channelForm.setInvoiceType(type);
        return channelForm;
    }

    // test for adding channel
    @Test
    public void testAddChannel(){
        ChannelData channelData = channelDto.addChannel(channelForm1);
        assertNotNull(channelData);
        assertEquals(StringUtil.toLowerCase(channelForm1.getChannelName()),channelData.getName());
        assertEquals(InvoiceType.CHANNEL.toString(),channelData.getInvoiceType());
    }

    // test for getting channel
    @Test
    public void testGetChannel(){
        ChannelData channelData1 = channelDto.addChannel(channelForm1);
        ChannelData channelData2 = channelDto.getChannel(channelData1.getId());
        assertNotNull(channelData2);
        assertEquals(StringUtil.toLowerCase(channelForm1.getChannelName()),channelData2.getName());
        assertEquals(InvoiceType.CHANNEL.toString(),channelData2.getInvoiceType());
    }

    // test for searching channel
    @Test
    public void testSearchChannel(){
        channelDto.addChannel(channelForm1);
        List<ChannelData> channelDataList = channelDto.searchChannels(channelForm2);
        assertEquals(1,channelDataList.size());
        // update form
        channelForm2.setInvoiceType("self");
        channelDataList = channelDto.searchChannels(channelForm2);
        assertEquals(0,channelDataList.size());
    }

    // test for updating channel
    @Test
    public void testUpdateChannel(){
        ChannelData channelData = channelDto.addChannel(channelForm1);
        channelForm2.setInvoiceType("self");
        ChannelData channelData1 = channelDto.updateChannel(channelData.getId(),channelForm2);
        assertNotNull(channelData1);
        assertEquals(StringUtil.toLowerCase(channelForm2.getChannelName()),channelData1.getName());
        assertEquals(channelForm2.getInvoiceType(),StringUtil.toLowerCase(channelData1.getInvoiceType()));
    }

    // test for getting all channels
    @Test
    public void testGetAll(){
        channelDto.addChannel(channelForm1);
        List<ChannelData> channelDataList = channelDto.getAllChannels();
        assertTrue(channelDataList.size()>0);
        assertEquals(1,channelDataList.size());
    }

    // test for validation
    @Test(expected = ApiException.class)
    public void testValidate(){
        channelDto.validate(channelForm1);
        // throw exception
        channelDto.validate(channelForm2);
    }

    // test for getting channel by client
    @Test
    public void testGetChannelByClient(){
        ChannelData channelData1 = channelDto.addChannel(channelForm1);
        channelListing1.setChannelId(channelData1.getId());
        List<ChannelListing> channelListing = new ArrayList<>();
        channelListing.add(channelListing1);
        channelDto.setChannelListingRestTemplate(channelListingApi);
        when(channelListingApi.getByClientId(1L)).thenReturn(channelListing);
        List<ChannelData> channelDataList = channelDto.getChannelByClient(1L);
        assertEquals(1,channelDataList.size());
    }

    // test for getting all clients
    @Test
    public void testGetAllClients(){
        channelDto.setClientAssureRestTemplate(clientAssure);
        List<ClientData> clientDataList = new ArrayList<>();
        clientDataList.add(clientData1);
        when(clientAssure.getClientDetails()).thenReturn(clientDataList);
        List<ClientData> clientDataList1 = channelDto.getAllClients();
        assertEquals(1,clientDataList1.size());
    }

    // test for getting all customers
    @Test
    public void testGetAllCustomers(){
        channelDto.setClientAssureRestTemplate(clientAssure);
        List<ClientData> clientDataList = new ArrayList<>();
        clientDataList.add(clientData2);
        when(clientAssure.getCustomerDetails()).thenReturn(clientDataList);
        List<ClientData> clientDataList1 = channelDto.getAllCustomers();
        assertEquals(1,clientDataList1.size());
    }

}
