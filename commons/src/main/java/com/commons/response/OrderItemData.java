package com.commons.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {
    private String clientSkuId;
    private String productName;
    private String brandId;
    private Long orderedQuantity;
    private double sellingPricePerUnit;
}
