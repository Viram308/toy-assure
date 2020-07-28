package com.commons.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private Long clientId;
    private String clientSkuId;
    private String brandId;
    private String productName;
    private double mrp;
    private String description;
}
