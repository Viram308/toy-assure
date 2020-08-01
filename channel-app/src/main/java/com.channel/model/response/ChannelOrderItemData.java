package com.channel.model.response;

import com.commons.response.OrderItemData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelOrderItemData extends OrderItemData {
    private String channelSkuId;
}
