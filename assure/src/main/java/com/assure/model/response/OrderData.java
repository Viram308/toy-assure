package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {
    private String clientName;
    private String customerName;
    private String channelName;
    private String channelOrderId;
    private String status;
}
