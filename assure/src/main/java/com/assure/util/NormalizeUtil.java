package com.assure.util;

import com.assure.model.form.ClientForm;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.commons.enums.ClientType;
import com.commons.util.StringUtil;

public class NormalizeUtil {

    public static void normalizeClient(Client client){
        client.setName(StringUtil.toLowerCase(client.getName()));
        client.setType(Enum.valueOf(ClientType.class,StringUtil.toUpperCase(client.getType().toString())));
    }

    public static void normalizeProduct(Product product) {
        product.setDescription(StringUtil.toLowerCase(product.getDescription()));
        product.setName(StringUtil.toLowerCase(product.getName()));
        product.setBrandId(StringUtil.toLowerCase(product.getBrandId()));
        product.setClientSkuId(StringUtil.toLowerCase(product.getClientSkuId()));
    }
}
