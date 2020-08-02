package com.channel.validator;

import com.channel.assure.ClientAssure;
import com.channel.assure.OrderAssure;
import com.channel.assure.ProductAssure;
import com.channel.dto.ChannelDto;
import com.commons.enums.ClientType;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderItemForm;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.response.OrderData;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ChannelOrderCsvFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(ChannelOrderCsvFormValidator.class);

    @Autowired
    private ClientAssure clientAssure;
    @Autowired
    private ProductAssure productAssure;
    @Autowired
    private OrderAssure orderAssure;
    @Autowired
    private ChannelDto channelDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChannelOrderCsvFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        logger.info("Validate");
        OrderCsvForm orderCsvForm = (OrderCsvForm) target;
        List<OrderItemForm> orderItemFormList = orderCsvForm.getOrderItemFormList();
        int index = 0;
        for (OrderItemForm itemForm : orderItemFormList) {
            logger.info("Validate item");
            Long clientId = itemForm.getClientId();
            Long customerId = itemForm.getCustomerId();
            Long channelId = itemForm.getChannelId();
            String channelOrderId = itemForm.getChannelOrderId();
            String clientSkuId = itemForm.getClientSkuId();
            logger.info("validate client");
            ClientData clientData = clientAssure.getClientData(clientId);
            if (clientData == null || !StringUtil.toUpperCase(clientData.getType()).equals(ClientType.CLIENT.toString())) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("clientId", "not found", "Client doesn't exist for clientId :" + clientId);
                errors.popNestedPath();
            }
            logger.info("validate customer");
            ClientData customerData = clientAssure.getClientData(customerId);
            if (customerData == null || !StringUtil.toUpperCase(customerData.getType()).equals(ClientType.CUSTOMER.toString())) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("customerId", "not found", "Customer doesn't exist for customerId :" + customerId);
                errors.popNestedPath();
            }
            logger.info("validate channel");
            ChannelData channelData = channelDto.getChannel(channelId);
            if (channelData == null) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("channelId", "not found", "Channel doesn't exist for channelId :" + channelId);
                errors.popNestedPath();
            }
            logger.info("validate order");
            OrderData orderData = orderAssure.getOrderDetails(channelOrderId, channelId);
            if (orderData != null) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("channelOrderId", "duplicate", "Order with channelOrderId :" + channelOrderId + " already exists");
                errors.popNestedPath();
            }
            logger.info("validate product");
            List<ProductData> productDataList = productAssure.getProductByClientIdAndClientSkuId(clientId);
            boolean check = false;
            for(ProductData productData : productDataList){
                if(productData.getClientSkuId().equals(StringUtil.toLowerCase(clientSkuId))){
                    check = true;
                    break;
                }
            }
            if (!check) {
                errors.pushNestedPath("orderItemFormList[" + index + "]");
                errors.rejectValue("clientSkuId", "not found", "Product doesn't exist for clientSkuId :" + clientSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
