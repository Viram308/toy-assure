package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinSkuUpdateForm {
    private Long binSkuId;
    private Long binId;
    private Long globalSkuId;
    private String productName;
    private String brandId;
    private Long originalQuantity;
    private Long updateQuantity;
}
