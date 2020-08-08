package com.channel.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListingForm {
    private Long clientId;
    private Long channelId;
    private String channelSkuId;
    private String clientSkuId;
}
