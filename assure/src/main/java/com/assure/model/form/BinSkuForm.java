package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinSkuForm {
    private Long binId;
    private String clientSkuId;
    private Long clientId;
    private Long quantity;
}
