package com.commons.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchForm {
    private Long clientId;
    private Long channelId;
    private String orderStatus;
}
