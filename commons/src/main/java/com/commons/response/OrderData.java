package com.commons.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {
    private Long orderId;
    private String clientName;
    private String customerName;
    private String channelName;
    private String channelOrderId;
    private String status;
    private Long channelId;
    private Long clientId;
}
