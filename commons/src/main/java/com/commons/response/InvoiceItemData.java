package com.commons.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemData {
    private Long id;
    private String productName;
    private String clientSkuId;
    private Long orderedQuantity;
    private double sellingPricePerUnit;
}
