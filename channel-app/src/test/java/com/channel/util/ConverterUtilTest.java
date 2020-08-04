package com.channel.util;

import com.channel.model.form.ChannelForm;
import com.channel.model.form.ChannelListingForm;
import com.channel.model.response.ChannelListingData;
import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.channel.spring.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import com.commons.response.ChannelData;
import com.commons.response.ProductData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConverterUtilTest extends AbstractUnitTest {
    private ChannelForm channelForm;
    private ChannelListingForm channelListingForm;
    @Before
    public void setUp(){
        channelForm = createChannelForm();
        channelListingForm = createChannelListingForm();
    }

    private ChannelListingForm createChannelListingForm() {
        ChannelListingForm channelListingForm = new ChannelListingForm();
        channelListingForm.setChannelId(2L);
        channelListingForm.setChannelSkuId("prod1");
        channelListingForm.setClientId(3L);
        channelListingForm.setClientSkuId("product1");
        return channelListingForm;
    }

    private ChannelForm createChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName("channel1");
        channelForm.setInvoiceType("channel");
        return channelForm;
    }

    @Test
    public void testConvertChannelFormToChannel(){
        Channel channel = ConverterUtil.convertChannelFormToChannel(channelForm);
        assertNotNull(channel);
        assertEquals(channelForm.getChannelName(),channel.getName());
        assertEquals(InvoiceType.CHANNEL,channel.getInvoiceType());
    }

    @Test
    public void testConvertChannelToChannelData(){
        Channel channel = ConverterUtil.convertChannelFormToChannel(channelForm);
        channel.setId(2L);
        ChannelData channelData=ConverterUtil.convertChannelToChannelData(channel);
        assertNotNull(channelData);
        assertEquals(channel.getId(),channelData.getId());
        assertEquals(channel.getName(),channelData.getName());
        assertEquals(channel.getInvoiceType().toString(),channelData.getInvoiceType());
    }

    @Test
    public void testConvertChannelListingFormToChannelListing(){
        ProductData productData = new ProductData();
        productData.setGlobalSkuId(6L);
        ChannelListing channelListing = ConverterUtil.convertChannelListingFormToChannelListing(channelListingForm,productData);
        assertNotNull(channelListing);
        assertEquals(channelListingForm.getChannelId(),channelListing.getChannelId());
        assertEquals(channelListingForm.getChannelSkuId(),channelListing.getChannelSkuId());
        assertEquals(channelListingForm.getClientId(),channelListing.getClientId());
        assertEquals(productData.getGlobalSkuId(),channelListing.getGlobalSkuId());
    }

    @Test
    public void testConvertChannelListingToChannelListingData(){
        ProductData productData = new ProductData();
        productData.setGlobalSkuId(6L);
        productData.setBrandId("nestle");
        productData.setClientName("viram");
        productData.setClientSkuId("prod1");
        productData.setProductName("munch");
        productData.setMrp(20.50);
        Channel channel = ConverterUtil.convertChannelFormToChannel(channelForm);
        channel.setId(2L);
        ChannelListing channelListing = ConverterUtil.convertChannelListingFormToChannelListing(channelListingForm,productData);
        ChannelListingData channelListingData = ConverterUtil.convertChannelListingToChannelListingData(channelListing,channel,productData);
        assertNotNull(channelListingData);
        assertEquals(productData.getBrandId(),channelListingData.getBrandId());
        assertEquals(channelListing.getChannelId(),channelListingData.getChannelId());
        assertEquals(channel.getName(),channelListingData.getChannelName());
        assertEquals(channelListing.getChannelSkuId(),channelListingData.getChannelSkuId());
        assertEquals(productData.getClientName(),channelListingData.getClientName());
        assertEquals(productData.getClientSkuId(),channelListingData.getClientSkuId());
        assertEquals(productData.getProductName(),channelListingData.getProductName());
        assertEquals(productData.getMrp(),channelListingData.getSellingPricePerUnit(),0.01);
    }
}
