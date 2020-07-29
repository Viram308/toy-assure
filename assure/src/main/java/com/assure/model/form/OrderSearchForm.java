package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchForm {
    private Long clientId;
    private Long customerId;
    private Long channelId;
}
