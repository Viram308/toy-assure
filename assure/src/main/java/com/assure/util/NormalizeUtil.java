package com.assure.util;

import com.assure.pojo.Client;
import com.assure.pojo.Order;
import com.assure.pojo.OrderItem;
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
        product.setMrp(Math.round(product.getMrp()*100)/100.0);
    }

    public static void normalizeOrder(Order order) {
        order.setChannelOrderId(StringUtil.toLowerCase(order.getChannelOrderId()));
    }

    public static void normalizeOrderItem(OrderItem orderItem) {
        orderItem.setSellingPricePerUnit(Math.round(orderItem.getSellingPricePerUnit()*100)/100.0);
    }
}
