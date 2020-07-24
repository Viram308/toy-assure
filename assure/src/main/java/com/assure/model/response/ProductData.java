package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData {
    private Long globalSkuId;
    private String clientSkuId;
    private String clientName;
    private String brandId;
    private String productName;
    private double mrp;
    private String description;
}
