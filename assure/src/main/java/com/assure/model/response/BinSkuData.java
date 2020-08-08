package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinSkuData {
    private Long binSkuId;
    private Long binId;
    private Long globalSkuId;
    private String clientName;
    private String clientSkuId;
    private String productName;
    private String brandId;
    private Long quantity;
}
