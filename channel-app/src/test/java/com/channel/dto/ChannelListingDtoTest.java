package com.channel.dto;

import com.channel.assure.ClientAssure;
import com.channel.assure.ProductAssure;
import com.channel.model.form.ChannelForm;
import com.channel.model.form.ChannelListingCsvForm;
import com.channel.model.form.ChannelListingForm;
import com.channel.model.form.ChannelListingSearchForm;
import com.channel.model.response.ChannelListingData;
import com.channel.spring.AbstractUnitTest;
import com.commons.api.CustomValidationException;
import com.commons.enums.ClientType;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChannelListingDtoTest extends AbstractUnitTest {

    private ChannelListingForm channelListingForm1, channelListingForm2;
    private ProductData productData1, productData2;
    private ClientData clientData;
    private final ChannelListingCsvForm channelListingCsvForm = new ChannelListingCsvForm();
    private ChannelForm channelForm1;
    private ChannelListingSearchForm channelListingSearchForm;


    @Autowired
    private ChannelListingDto channelListingDto;
    @Autowired
    private ChannelDto channelDto;

    @Mock
    private ClientAssure clientAssure;

    @Mock
    private ProductAssure productAssure;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        channelForm1 = createChannelForm();
        channelListingForm1 = createChannelListingForm("prod1", "channelProd1");
        channelListingForm2 = createChannelListingForm("prod2", "channelProd2");
        clientData = createClientData();
        productData1 = createProductData("prod1", 1L, "munch", 10.50);
        productData2 = createProductData("prod2", 2L, "kitkat", 15);
        channelListingSearchForm = createChannelListingSearchForm();
    }

    private ChannelListingSearchForm createChannelListingSearchForm() {
        ChannelListingSearchForm channelListingSearchForm = new ChannelListingSearchForm();
        channelListingSearchForm.setClientId(1L);
        return channelListingSearchForm;
    }

    private ChannelForm createChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName("channel1");
        channelForm.setInvoiceType("CHANNEL");
        return channelForm;
    }

    private ClientData createClientData() {
        ClientData clientData = new ClientData();
        clientData.setId(1L);
        clientData.setName("viram");
        clientData.setType(ClientType.CLIENT.toString());
        return clientData;
    }

    private ProductData createProductData(String clientSkuId, Long globalSkuId, String name, double mrp) {
        ProductData productData = new ProductData();
        productData.setClientSkuId(clientSkuId);
        productData.setGlobalSkuId(globalSkuId);
        productData.setProductName(name);
        productData.setClientName(clientData.getName());
        productData.setBrandId("nestle");
        productData.setMrp(mrp);
        return productData;
    }

    private ChannelListingForm createChannelListingForm(String clientSkuId, String channelSkuId) {
        ChannelListingForm channelListingForm = new ChannelListingForm();
        channelListingForm.setClientId(1L);
        channelListingForm.setClientSkuId(clientSkuId);
        channelListingForm.setChannelSkuId(channelSkuId);
        return channelListingForm;
    }

    private void addDetails() {
        // add channel
        ChannelData channelData = channelDto.addChannel(channelForm1);
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();
        // set channel id
        channelListingForm1.setChannelId(channelData.getId());
        channelListingForm2.setChannelId(channelData.getId());
        channelListingSearchForm.setChannelId(channelData.getId());
        channelListingFormList.add(channelListingForm1);
        channelListingFormList.add(channelListingForm2);
        channelListingCsvForm.setChannelListingFormList(channelListingFormList);
        // set restTemplate
        channelListingDto.setClientAssureRestTemplate(clientAssure);
        channelListingDto.setProductAssureRestTemplate(productAssure);
        List<ProductData> productDataList = new ArrayList<>();
        productDataList.add(productData1);
        productDataList.add(productData2);
        // rules
        when(clientAssure.getClientData(1L)).thenReturn(clientData);
        when(productAssure.getProductByClientIdAndClientSkuId(1L)).thenReturn(productDataList);
        when(productAssure.getProductData(1L)).thenReturn(productData1);
        when(productAssure.getProductData(2L)).thenReturn(productData2);
    }

    // test for adding channel listing
    @Test(expected = CustomValidationException.class)
    public void testAddChannelListing() {
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // add
        channelListingDto.addChannelListing(channelListingCsvForm, result);
        when(result.hasErrors()).thenReturn(true);
        channelListingDto.addChannelListing(channelListingCsvForm, result);
    }

    // test for searching channel listing
    @Test
    public void testSearch() {
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelListingDto.addChannelListing(channelListingCsvForm, result);
        List<ChannelListingData> channelListingDataList = channelListingDto.searchChannelListing(channelListingSearchForm);
        assertEquals(2, channelListingDataList.size());
    }

    // test for getting all channel listing
    @Test
    public void testGetAllChannelListing() {
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelListingDto.addChannelListing(channelListingCsvForm, result);
        List<ChannelListingData> channelListingDataList = channelListingDto.getAllChannelListing();
        assertEquals(2, channelListingDataList.size());
    }

    // test for getting channel listing by id
    @Test
    public void testGetChannelListing() {
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelListingDto.addChannelListing(channelListingCsvForm, result);
        List<ChannelListingData> channelListingDataList = channelListingDto.getAllChannelListing();
        // get channel listing

        ChannelListingData channelListingData1 = channelListingDto.getChannelListing(channelListingDataList.get(0).getId());
        // get channel listing
        ChannelListingData channelListingData2 = channelListingDto.getChannelListing(channelListingDataList.get(1).getId());
        // test data
        assertEquals(StringUtil.toLowerCase(channelListingForm1.getChannelSkuId()), channelListingData1.getChannelSkuId());
        assertEquals(channelListingForm1.getChannelId(), channelListingData1.getChannelId());
        assertEquals(StringUtil.toLowerCase(channelListingForm1.getClientSkuId()), channelListingData1.getClientSkuId());
        assertEquals(StringUtil.toLowerCase(channelListingForm2.getChannelSkuId()), channelListingData2.getChannelSkuId());
        assertEquals(channelListingForm2.getChannelId(), channelListingData2.getChannelId());
        assertEquals(StringUtil.toLowerCase(channelListingForm2.getClientSkuId()), channelListingData2.getClientSkuId());
    }

    // test for getting channel listing by channelId,channelSkuId and clientId
    @Test
    public void testGetChannelListingData() {
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelListingDto.addChannelListing(channelListingCsvForm, result);
        ChannelListingData channelListingData = channelListingDto.getChannelListingData(channelListingForm1.getChannelId(), channelListingForm1.getChannelSkuId(), channelListingForm1.getClientId());
        assertNotNull(channelListingData);
        // test data
        assertEquals(StringUtil.toLowerCase(channelListingForm1.getChannelSkuId()), channelListingData.getChannelSkuId());
        assertEquals(channelListingForm1.getChannelId(), channelListingData.getChannelId());
        assertEquals(StringUtil.toLowerCase(channelListingForm1.getClientSkuId()), channelListingData.getClientSkuId());
    }

    // test for getting channel listing by channel and client
    @Test
    public void testGetAllByChannelClient() {
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelListingDto.addChannelListing(channelListingCsvForm, result);
        List<ChannelListingData> channelListingDataList = channelListingDto.getAllChannelListingByChannelClient(channelListingForm1.getChannelId(),channelListingForm1.getClientId());
        // test list size
        assertEquals(2,channelListingDataList.size());
    }

}