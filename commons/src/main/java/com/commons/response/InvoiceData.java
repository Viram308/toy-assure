package com.commons.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private Long orderId;
    private String date;
    private String time;
    private String clientName;
    private String customerName;
    private String channelName;
    private String invoiceType;
    private List<InvoiceItemData> invoiceItemDataList;
}
