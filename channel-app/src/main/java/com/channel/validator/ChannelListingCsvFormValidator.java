package com.channel.validator;

import com.channel.assure.ClientAssure;
import com.channel.assure.ProductAssure;
import com.channel.dto.ChannelDto;
import com.channel.dto.ChannelListingDto;
import com.channel.model.form.ChannelListingCsvForm;
import com.channel.model.form.ChannelListingForm;
import com.commons.response.ChannelData;
import com.channel.model.response.ChannelListingData;
import com.commons.enums.ClientType;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;

@Component
public class ChannelListingCsvFormValidator implements Validator {

    @Autowired
    private ChannelListingDto channelListingDto;
    @Autowired
    private ChannelDto channelDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChannelListingCsvFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChannelListingCsvForm channelListingCsvForm = (ChannelListingCsvForm) target;
        List<ChannelListingForm> channelListingFormList = channelListingCsvForm.getChannelListingFormList();
        int index = 0;
        HashMap<String, Integer> checkDuplicateChannelSku = new HashMap<>();
        HashMap<String, Integer> checkDuplicateClientSku = new HashMap<>();
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            Long clientId = channelListingForm.getClientId();
            Long channelId = channelListingForm.getChannelId();
            String clientSkuId = channelListingForm.getClientSkuId();
            String channelSkuId = channelListingForm.getChannelSkuId();
            if (checkDuplicateClientSku.containsKey(clientSkuId)) {
                errors.pushNestedPath("channelListingFormList[" + index + "]");
                errors.rejectValue("clientSkuId", "duplicate", "clientSkuId already present in csv file");
                errors.popNestedPath();
            } else {
                checkDuplicateClientSku.put(clientSkuId, 1);
            }
            if (checkDuplicateChannelSku.containsKey(channelSkuId)) {
                errors.pushNestedPath("channelListingFormList[" + index + "]");
                errors.rejectValue("channelSkuId", "duplicate", "channelSkuId already present in csv file");
                errors.popNestedPath();
            } else {
                checkDuplicateChannelSku.put(channelSkuId, 1);
            }
            ClientData clientData = channelListingDto.getClient(clientId);
            if (clientData == null || !StringUtil.toUpperCase(clientData.getType()).equals(ClientType.CLIENT.toString())) {
                errors.pushNestedPath("channelListingFormList[" + index + "]");
                errors.rejectValue("clientId", "not found", "Client doesn't exist for clientId :" + clientId);
                errors.popNestedPath();
            }
            List<ProductData> productDataList = channelListingDto.getProductByClientIdAndClientSkuId(clientId);
            boolean matchFound = false;
            for (ProductData productData : productDataList) {
                if (productData.getClientSkuId().equals(clientSkuId)) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                errors.pushNestedPath("channelListingFormList[" + index + "]");
                errors.rejectValue("clientSkuId", "not found", "product doesn't exist for clientSkuId : " + clientSkuId + " and clientId :" + clientId);
                errors.popNestedPath();
            }
            ChannelData channelData = channelDto.getChannel(channelId);
            if (channelData == null) {
                errors.pushNestedPath("channelListingFormList[" + index + "]");
                errors.rejectValue("channelId", "not found", "channel doesn't exist for channelId : " + channelId);
                errors.popNestedPath();
            }
            ChannelListingData channelListingData = channelListingDto.getChannelListingData(channelId, channelSkuId, clientId);
            if (channelListingData != null) {
                errors.pushNestedPath("channelListingFormList[" + index + "]");
                errors.rejectValue("channelSkuId", "duplicate", "channel listing already present for channelId : " + channelId + " ,channelSkuId : " + channelSkuId + " ,clientId : " + clientId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}