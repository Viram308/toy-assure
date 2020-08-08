package com.channel.model.response;

import com.commons.response.OrderItemData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelOrderItemData {
    private String clientSkuId;
    private String productName;
    private String brandId;
    private Long orderedQuantity;
    private double sellingPricePerUnit;
    private String channelSkuId;
}
