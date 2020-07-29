package com.assure.validator;

import com.assure.dto.ClientDto;
import com.assure.dto.OrderDto;
import com.assure.dto.ProductDto;
import com.assure.model.form.OrderCsvForm;
import com.assure.model.form.OrderItemForm;
import com.assure.model.response.OrderData;
import com.commons.enums.ClientType;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class OrderCsvFormValidator implements Validator {

    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private OrderDto orderDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderCsvFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderCsvForm orderCsvForm = (OrderCsvForm) target;
        List<OrderItemForm> orderItemFormList = orderCsvForm.getOrderItemFormList();
        int index = 0;
        for (OrderItemForm itemForm : orderItemFormList) {
            Long clientId = itemForm.getClientId();
            Long customerId = itemForm.getCustomerId();
            Long channelId = itemForm.getChannelId();
            String channelOrderId = itemForm.getChannelOrderId();
            String clientSkuId = itemForm.getClientSkuId();

            ClientData clientData = clientDto.getClient(clientId);
            if (clientData == null || !StringUtil.toUpperCase(clientData.getType()).equals(ClientType.CLIENT.toString())) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("clientId", "not found", "Client doesn't exist for clientId :" + clientId);
                errors.popNestedPath();
            }
            clientData = clientDto.getClient(customerId);
            if (clientData == null || !StringUtil.toUpperCase(clientData.getType()).equals(ClientType.CUSTOMER.toString())) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("customerId", "not found", "Customer doesn't exist for customerId :" + customerId);
                errors.popNestedPath();
            }
            ChannelData channelData = orderDto.getChannelDetails(channelId);
            if(channelData == null){
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("channelId", "not found", "Channel doesn't exist for channelId :" + channelId);
                errors.popNestedPath();
            }
            OrderData orderData = orderDto.getOrderDetails(channelOrderId,channelId);
            if(orderData != null){
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("channelOrderId", "duplicate", "Order with channelOrderId :" + channelOrderId + " already exists");
                errors.popNestedPath();
            }
            ProductData productData = productDto.getProductByClientIdAndClientSkuId(clientId,clientSkuId);
            if(productData == null){
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("clientSkuId", "not found", "Product doesn't exist for clientSkuId :" + clientSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
