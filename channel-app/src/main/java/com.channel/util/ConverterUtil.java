package com.channel.util;

import com.channel.model.form.ChannelForm;
import com.channel.model.form.ChannelListingForm;
import com.commons.response.ChannelData;
import com.channel.model.response.ChannelListingData;
import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.commons.enums.InvoiceType;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;

public class ConverterUtil {
    public static Channel convertChannelFormToChannel(ChannelForm channelForm){
        Channel channel = new Channel();
        channel.setName(channelForm.getChannelName());
        channel.setInvoiceType(Enum.valueOf(InvoiceType.class, StringUtil.toUpperCase(channelForm.getInvoiceType())));
        return channel;
    }

    public static ChannelData convertChannelToChannelData(Channel channel) {
        ChannelData channelData = new ChannelData();
        channelData.setId(channel.getId());
        channelData.setName(channel.getName());
        channelData.setInvoiceType(channel.getInvoiceType().toString());
        return channelData;
    }

    public static ChannelListingData convertChannelListingToChannelListingData(ChannelListing channelListing, Channel channel, ProductData productData) {
        ChannelListingData channelListingData = new ChannelListingData();
        channelListingData.setChannelName(channel.getName());
        channelListingData.setChannelSkuId(channelListing.getChannelSkuId());
        channelListingData.setProductName(productData.getProductName());
        channelListingData.setBrandId(productData.getBrandId());
        channelListingData.setClientName(productData.getClientName());
        return channelListingData;
    }

    public static ChannelListing convertChannelListingFormToChannelListing(ChannelListingForm channelListingForm,ProductData productData) {
        ChannelListing channelListing =new ChannelListing();
        channelListing.setChannelId(channelListingForm.getChannelId());
        channelListing.setChannelSkuId(channelListingForm.getChannelSkuId());
        channelListing.setClientId(channelListingForm.getClientId());
        channelListing.setGlobalSkuId(productData.getGlobalSkuId());
        return channelListing;
    }
}
