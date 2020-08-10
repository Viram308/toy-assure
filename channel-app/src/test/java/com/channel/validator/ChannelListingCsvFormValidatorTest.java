package com.channel.validator;

import com.channel.assure.ClientAssure;
import com.channel.assure.ProductAssure;
import com.channel.dto.ChannelDto;
import com.channel.dto.ChannelListingDto;
import com.channel.model.form.ChannelForm;
import com.channel.model.form.ChannelListingCsvForm;
import com.channel.model.form.ChannelListingForm;
import com.channel.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ChannelListingCsvFormValidatorTest extends AbstractUnitTest {
    private final ChannelListingCsvForm channelListingCsvForm = new ChannelListingCsvForm();
    private ChannelListingForm channelListingForm1, channelListingForm2;
    private ChannelForm channelForm;
    private ProductData productData1, productData2;
    private ClientData clientData;

    @Mock
    private ClientAssure clientAssure;

    @Mock
    private ProductAssure productAssure;
    @Autowired
    private ChannelListingDto channelListingDto;
    @Autowired
    private ChannelListingCsvFormValidator channelListingCsvFormValidator;

    @Autowired
    private ChannelDto channelDto;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        channelForm = createChannelForm();
        channelListingForm1 = createChannelListingForm();
        channelListingForm2 = createChannelListingForm();
        clientData = createClientData();
        productData1 = createProductData("prod1", 1L, "munch", 10.50);
        productData2 = createProductData("prod2", 2L, "kitkat", 15);

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

    private ChannelForm createChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName("channel1");
        channelForm.setInvoiceType("CHANNEL");
        return channelForm;
    }

    private ChannelListingForm createChannelListingForm() {
        ChannelListingForm channelListingForm = new ChannelListingForm();
        channelListingForm.setClientId(1L);
        channelListingForm.setClientSkuId("prod1");
        channelListingForm.setChannelSkuId("channelProd1");
        return channelListingForm;
    }

    public void addDetails(){
        // set restTemplate
        channelListingDto.setClientAssureRestTemplate(clientAssure);
        channelListingDto.setProductAssureRestTemplate(productAssure);
        // rules
        when(clientAssure.getClientData(1L)).thenReturn(null);
        when(productAssure.getProductByClientIdAndClientSkuId(1L)).thenReturn(new ArrayList<>());
        when(productAssure.getProductData(1L)).thenReturn(productData1);
        when(productAssure.getProductData(2L)).thenReturn(productData2);
    }

    // test validate function
    @Test
    public void testValidate(){
        addDetails();
        ChannelData channelData = channelDto.addChannel(channelForm);
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();
        // set channel id
        channelListingForm1.setChannelId(channelData.getId());
        channelListingForm2.setChannelId(channelData.getId());
        channelListingFormList.add(channelListingForm1);
        channelListingFormList.add(channelListingForm2);
        channelListingCsvForm.setChannelListingFormList(channelListingFormList);
        Errors errors = new BeanPropertyBindingResult(channelListingCsvForm,"Invalid Csv");
        channelListingCsvFormValidator.validate(channelListingCsvForm,errors);
        assertTrue(errors.hasErrors());
        // test no.of errors
        assertEquals(6,errors.getErrorCount());

    }
}
