package com.commons.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private Long clientId;
    private Long customerId;
    private Long channelId;
    private String channelOrderId;
    private String clientSkuId;
    private Long orderedQuantity;
    private double sellingPricePerUnit;
}
