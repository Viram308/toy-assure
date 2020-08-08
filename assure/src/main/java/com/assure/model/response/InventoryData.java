package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData {
    private String clientName;
    private String clientSkuId;
    private String productName;
    private String brandId;
    private Long availableQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
}
