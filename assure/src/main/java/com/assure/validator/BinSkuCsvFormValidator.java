package com.assure.validator;

import com.assure.dto.BinDto;
import com.assure.dto.ClientDto;
import com.assure.dto.ProductDto;
import com.assure.model.form.BinSkuCsvForm;
import com.assure.model.form.BinSkuForm;
import com.assure.pojo.Bin;
import com.commons.enums.ClientType;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class BinSkuCsvFormValidator implements Validator {

    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BinDto binDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return BinSkuCsvFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BinSkuCsvForm binSkuCsvForm = (BinSkuCsvForm) target;
        List<BinSkuForm> binSkuFormList = binSkuCsvForm.getBinSkuFormList();
        int index = 0;
        for (BinSkuForm binSkuForm : binSkuFormList) {
            Long binId=binSkuForm.getBinId();
            Bin bin = binDto.getBin(binId);
            if(bin==null){
                errors.pushNestedPath("binSkuFormList[" + index + "]");
                errors.rejectValue("binId", "not found", "Bin doesn't exist for binId :" + binId);
                errors.popNestedPath();
            }
            Long clientId = binSkuForm.getClientId();
            ClientData clientData = clientDto.getClient(clientId);
            if (clientData == null || !StringUtil.toUpperCase(clientData.getType()).equals(ClientType.CLIENT.toString())) {
                errors.pushNestedPath("binSkuFormList[" + index + "]");
                errors.rejectValue("clientId", "not found", "Client doesn't exist for clientId :" + clientId);
                errors.popNestedPath();
            }
            String clientSkuId = binSkuForm.getClientSkuId();
            ProductData productData = productDto.getProductByClientIdAndClientSkuId(clientId, clientSkuId);
            if (productData == null) {
                errors.pushNestedPath("binSkuFormList[" + index + "]");
                errors.rejectValue("clientSkuId", "not found", "product doesn't exist for clientSkuId : " + clientSkuId + " and clientId :" + clientId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
