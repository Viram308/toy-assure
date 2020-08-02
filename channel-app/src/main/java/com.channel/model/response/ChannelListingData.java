package com.channel.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListingData {
    private String channelName;
    private String channelSkuId;
    private String clientName;
    private String clientSkuId;
    private String productName;
    private String brandId;
    private double sellingPricePerUnit;
    private Long channelId;
    private Long id;
}
