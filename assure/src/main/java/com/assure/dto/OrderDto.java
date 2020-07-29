package com.assure.dto;

import com.assure.api.*;
import com.assure.channel.ChannelDataApi;
import com.assure.model.form.OrderCsvForm;
import com.assure.model.form.OrderSearchForm;
import com.assure.model.response.OrderData;
import com.assure.model.response.OrderItemData;
import com.assure.pojo.BinSku;
import com.assure.pojo.Inventory;
import com.assure.pojo.Order;
import com.assure.pojo.OrderItem;
import com.assure.util.ConverterUtil;
import com.assure.validator.OrderCsvFormValidator;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import com.commons.enums.OrderStatus;
import com.commons.response.ChannelData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDto {

    private static final Logger logger = Logger.getLogger(OrderDto.class);

    @Autowired
    private OrderCsvFormValidator orderCsvFormValidator;
    @Autowired
    private ChannelDataApi channelDataApi;
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private ClientApi clientApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private BinSkuApi binSkuApi;

    @Transactional(rollbackFor = CustomValidationException.class)
    public List<OrderData> addOrder(OrderCsvForm orderCsvForm, BindingResult result) {
        orderCsvFormValidator.validate(orderCsvForm, result);
        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }
        Order order = ConverterUtil.convertOrderCsvFormToOrder(orderCsvForm);
        if (order != null) {
            order.setStatus(OrderStatus.CREATED);
            orderApi.addOrder(order);
        }
        logger.info("Allocate Orders");
        return allocateOrders();
    }

    @Transactional(readOnly = true)
    public ChannelData getChannelDetails(Long channelId) {
        return channelDataApi.getChannelDetails(channelId);
    }

    @Transactional(readOnly = true)
    public OrderData getOrderDetails(String channelOrderId, Long channelId) {
        Order order = orderApi.getOrderByChannelOrderIdAndChannelId(channelOrderId, channelId);
        return ConverterUtil.convertOrderToOrderData(order, clientApi.get(order.getClientId()), clientApi.get(order.getCustomerId()), channelDataApi.getChannelDetails(order.getChannelId()));
    }

    @Transactional
    public List<OrderData> allocateOrders() {
        int recordUpdated = orderAllocation();
        logger.info("Records updated : "+recordUpdated);
        return getAllOrders();
    }

    @Transactional
    public int orderAllocation() {
        int count = 0;
        List<Order> orderList = orderApi.getOrdersByStatus(OrderStatus.CREATED);
        if (orderList.isEmpty()) {
            return 0;
        }
        for (Order order : orderList) {
            boolean result = checkOrderInventoryStatus(order);
            if (result) {
                logger.info("Changing the status of the order(" + order.getId() + ") to Allocated.");
                order.setStatus(OrderStatus.ALLOCATED);
                orderApi.updateOrder(order);
                count++;
            }
        }
        return count;
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean checkOrderInventoryStatus(Order order) {
        List<OrderItem> itemPojoList = orderItemApi.getOrderItemByOrderId(order.getId());
        boolean orderCheck = true;
        for (OrderItem item : itemPojoList) {
            boolean check = orderItemAllocationLogic(item);
            if (!check) {
                orderCheck = false;
            }
        }
        return orderCheck;
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean orderItemAllocationLogic(OrderItem orderItem) {
        Inventory inventory = inventoryApi.getInventoryByGlobalSkuId(orderItem.getGlobalSkuId());
        List<Long> globalSkuIdList = new ArrayList<>();
        globalSkuIdList.add(orderItem.getGlobalSkuId());
        List<BinSku> skuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        if (skuList == null) {
            throw new ApiException("No BinSku records found.");
        }
        long remaining_available_quantity = Math.subtractExact(inventory.getAvailableQuantity(), orderItem.getOrderedQuantity());
        Long ordered_quantity = orderItem.getOrderedQuantity();
        if (remaining_available_quantity >= 0) {
            inventory.setAvailableQuantity(remaining_available_quantity);
            inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() + orderItem.getOrderedQuantity());
            orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + ordered_quantity);
            long min_Val;
            for (BinSku binSku : skuList) {
                if (ordered_quantity > 0) {
                    if (binSku.getQuantity() >= ordered_quantity) {
                        binSku.setQuantity(Math.subtractExact(binSku.getQuantity(), ordered_quantity));
                        ordered_quantity = 0L;
                    } else {
                        min_Val = Math.min(binSku.getQuantity(), ordered_quantity);
                        ordered_quantity = Math.subtractExact(ordered_quantity, min_Val);
                        binSku.setQuantity(0L);
                    }
                }
                binSkuApi.update(binSku.getId(),binSku);
            }
        }
        inventoryApi.update(inventory.getId(),inventory);
        orderItemApi.updateOrderItem(orderItem.getId(),orderItem);
        return orderItem.getAllocatedQuantity().equals(orderItem.getOrderedQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderData> getAllOrders() {
        List<Order> orderList = orderApi.getAll();
        return orderList.stream().map(o -> ConverterUtil.convertOrderToOrderData(o, clientApi.get(o.getClientId()), clientApi.get(o.getCustomerId()), channelDataApi.getChannelDetails(o.getChannelId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderData> searchOrder(OrderSearchForm orderSearchForm) {
        List<Order> orderList = orderApi.getAll();
        if(orderSearchForm.getClientId()!=0){
            orderList = orderList.stream().filter(o->(o.getClientId().equals(orderSearchForm.getClientId()))).collect(Collectors.toList());
        }
        if(orderSearchForm.getCustomerId()!=0){
            orderList = orderList.stream().filter(o->(o.getCustomerId().equals(orderSearchForm.getCustomerId()))).collect(Collectors.toList());
        }
        if(orderSearchForm.getChannelId()!=0){
            orderList = orderList.stream().filter(o->(o.getChannelId().equals(orderSearchForm.getChannelId()))).collect(Collectors.toList());
        }
        return orderList.stream().map(o -> ConverterUtil.convertOrderToOrderData(o, clientApi.get(o.getClientId()), clientApi.get(o.getCustomerId()), channelDataApi.getChannelDetails(o.getChannelId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderItemData> getOrderItems(Long id) {
        List<OrderItem> orderItemList = orderItemApi.getOrderItemByOrderId(id);
        return orderItemList.stream().map(o->ConverterUtil.convertOrderItemToOrderItemData(o,productApi.get(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    public List<OrderItemData> fulfillOrder(Long id, Long channelId) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<ChannelData> getAllChannels() {
        return channelDataApi.getAllChannel();
    }
}
