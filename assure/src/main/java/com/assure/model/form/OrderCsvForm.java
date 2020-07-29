package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCsvForm {
    private List<OrderItemForm> orderItemFormList;
}
