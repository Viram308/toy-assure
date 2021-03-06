package com.assure.dto;

import com.assure.api.*;
import com.assure.channel.ChannelDataApi;
import com.assure.pojo.*;
import com.assure.util.ConverterUtil;
import com.assure.validator.OrderCsvFormValidator;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import com.commons.enums.InvoiceType;
import com.commons.enums.OrderStatus;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderItemForm;
import com.commons.form.OrderSearchForm;
import com.commons.response.*;
import com.commons.util.PDFHandler;
import com.commons.util.StringUtil;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDto {

    private static final Logger logger = Logger.getLogger(OrderDto.class);
    private static final String PDF_PATH = "src/main/resources/com/assure/";
    private static final String INVOICE_TEMPLATE_XSL = "src/main/resources/com/assure/invoiceTemplate.xsl";

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
        // validate
        orderCsvFormValidator.validate(orderCsvForm, result);
        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }

        Order order = ConverterUtil.convertOrderCsvFormToOrder(orderCsvForm);
        if (order != null) {
            order = orderApi.addOrder(order);
            for (OrderItemForm orderItemForm : orderCsvForm.getOrderItemFormList()) {
                Product product = productApi.getByClientIdAndClientSkuId(order.getClientId(), orderItemForm.getClientSkuId());
                OrderItem orderItem = ConverterUtil.convertFormToOrderItemPojo(orderItemForm, product.getGlobalSkuId(), order.getId());
                orderItemApi.addOrderItem(orderItem);
            }
            logger.info("Allocate Orders");
            // allocate if available
            return allocateOrder(order.getId());
        }
        return getAllOrders();

    }

    @Transactional
    public void addChannelOrder(OrderCsvForm orderCsvForm) {
        Order order = ConverterUtil.convertOrderCsvFormToOrder(orderCsvForm);
        if (order != null) {
            orderApi.addOrder(order);
            logger.info("order created");
            for (OrderItemForm orderItemForm : orderCsvForm.getOrderItemFormList()) {
                Product product = productApi.getByClientIdAndClientSkuId(order.getClientId(), orderItemForm.getClientSkuId());
                OrderItem orderItem = ConverterUtil.convertFormToOrderItemPojo(orderItemForm, product.getGlobalSkuId(), order.getId());
                orderItemApi.addOrderItem(orderItem);
            }
        }
    }


    @Transactional(readOnly = true)
    public ChannelData getChannelDetails(Long channelId) {
        return channelDataApi.getChannelDetails(channelId);
    }

    @Transactional(readOnly = true)
    public OrderData getOrderDetails(String channelOrderId, Long channelId) {
        Order order = orderApi.getOrderByChannelOrderIdAndChannelId(channelOrderId, channelId);
        if (order != null) {
            return ConverterUtil.convertOrderToOrderData(order, clientApi.get(order.getClientId()), clientApi.get(order.getCustomerId()), channelDataApi.getChannelDetails(order.getChannelId()));
        }
        return null;
    }

    @Transactional
    public List<OrderData> allocateOrder(Long orderId) {
        Order order = orderApi.get(orderId);
        if(order.getStatus().equals(OrderStatus.CREATED)){
            boolean result = checkOrderInventoryStatus(order);
            if (result) {
                logger.info("Changing the status of the order(" + order.getId() + ") to Allocated.");
                order.setStatus(OrderStatus.ALLOCATED);
                orderApi.updateOrder(order);
            }
        }
        return getAllOrders();
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean checkOrderInventoryStatus(Order order) {
        List<OrderItem> itemPojoList = orderItemApi.getOrderItemByOrderId(order.getId());
        boolean orderCheck = true;
        for (OrderItem item : itemPojoList) {
            // check for every line item
            boolean check = orderItemAllocationLogic(item);
            if (!check) {
                orderCheck = false;
            }
        }
        return orderCheck;
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean orderItemAllocationLogic(OrderItem orderItem) {
        long quantityToAdd = orderItem.getOrderedQuantity() - orderItem.getAllocatedQuantity();
        if (quantityToAdd == 0) {
            return true;
        }
        Inventory inventory = inventoryApi.getInventoryByGlobalSkuId(orderItem.getGlobalSkuId());
        List<Long> globalSkuIdList = new ArrayList<>();
        globalSkuIdList.add(orderItem.getGlobalSkuId());
        List<BinSku> skuList = binSkuApi.searchByGlobalSkuIdList(globalSkuIdList);
        if (skuList == null) {
            throw new ApiException("No BinSku records found.");
        }
        // check for available quantity
        long remaining_available_quantity = Math.subtractExact(inventory.getAvailableQuantity(), quantityToAdd);
        if (remaining_available_quantity < 0) {
            quantityToAdd = inventory.getAvailableQuantity();
            // zero inventory
            inventory.setAvailableQuantity(0L);
        } else {
            inventory.setAvailableQuantity(remaining_available_quantity);
        }
        inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() + quantityToAdd);
        orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + quantityToAdd);
        long min_Val;
        // check for all bins with particular globalSkuId
        for (BinSku binSku : skuList) {
            if (quantityToAdd > 0) {
                if (binSku.getQuantity() >= quantityToAdd) {
                    binSku.setQuantity(Math.subtractExact(binSku.getQuantity(), quantityToAdd));
                    quantityToAdd = 0L;
                } else {
                    min_Val = Math.min(binSku.getQuantity(), quantityToAdd);
                    quantityToAdd = Math.subtractExact(quantityToAdd, min_Val);
                    binSku.setQuantity(0L);
                }
            }
            binSkuApi.update(binSku.getId(), binSku);
        }
        // update data
        inventoryApi.update(inventory.getId(), inventory);
        orderItemApi.updateOrderItem(orderItem.getId(), orderItem);
        return orderItem.getAllocatedQuantity().equals(orderItem.getOrderedQuantity());
    }

    @Transactional(readOnly = true)
    public OrderData get(Long id) {
        Order order = orderApi.get(id);
        return ConverterUtil.convertOrderToOrderData(order, clientApi.get(order.getClientId()), clientApi.get(order.getCustomerId()), channelDataApi.getChannelDetails(order.getChannelId()));
    }

    @Transactional(readOnly = true)
    public List<OrderData> getAllOrders() {
        List<Order> orderList = orderApi.getAll();
        return orderList.stream().map(o -> ConverterUtil.convertOrderToOrderData(o, clientApi.get(o.getClientId()), clientApi.get(o.getCustomerId()), channelDataApi.getChannelDetails(o.getChannelId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderData> searchOrder(OrderSearchForm orderSearchForm) {
        List<Order> orderList = orderApi.getAll();
        // check with clientId
        if (orderSearchForm.getClientId() != 0) {
            orderList = orderList.stream().filter(o -> (o.getClientId().equals(orderSearchForm.getClientId()))).collect(Collectors.toList());
        }
        // check with status
        if (!StringUtil.isEmpty(orderSearchForm.getOrderStatus())) {
            orderList = orderList.stream().filter(o -> (o.getStatus().toString().equals(StringUtil.toUpperCase(orderSearchForm.getOrderStatus())))).collect(Collectors.toList());
        }
        // check with channelId
        if (orderSearchForm.getChannelId() != 0) {
            orderList = orderList.stream().filter(o -> (o.getChannelId().equals(orderSearchForm.getChannelId()))).collect(Collectors.toList());
        }
        return orderList.stream().map(o -> ConverterUtil.convertOrderToOrderData(o, clientApi.get(o.getClientId()), clientApi.get(o.getCustomerId()), channelDataApi.getChannelDetails(o.getChannelId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderItemData> getOrderItems(Long id) {
        List<OrderItem> orderItemList = orderItemApi.getOrderItemByOrderId(id);
        return orderItemList.stream().map(o -> ConverterUtil.convertOrderItemToOrderItemData(o, productApi.get(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ApiException.class)
    public void fulfillOrder(Long id){
        Order order = orderApi.get(id);
        // check for allocated orders
        if(!order.getStatus().equals(OrderStatus.ALLOCATED)){
            throw new ApiException("Order is not allocated with id : "+id);
        }
        boolean result = orderItemFulfillmentLogic(order);
        if (!result) {
            throw new ApiException("Can't fulfill order for orderId : " + id);
        }
        order.setStatus(OrderStatus.FULFILLED);
        orderApi.updateOrder(order);
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean orderItemFulfillmentLogic(Order order) {
        List<OrderItem> orderItemList = orderItemApi.getOrderItemByOrderId(order.getId());
        for (OrderItem orderItem : orderItemList) {

            Long ordered_quantity = orderItem.getOrderedQuantity();
            Long allocated_quantity = orderItem.getAllocatedQuantity();
            if ((allocated_quantity - ordered_quantity) < 0) {
                throw new ApiException("OrderItem with orderItemId : " + orderItem.getId() + " is not allocated");
            }
            // transfer allocated quantity to fulfilled quantty
            Inventory inventory = inventoryApi.getInventoryByGlobalSkuId(orderItem.getGlobalSkuId());
            inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() - ordered_quantity);
            inventory.setFulfilledQuantity(inventory.getFulfilledQuantity() + ordered_quantity);
            inventoryApi.update(inventory.getId(), inventory);
            orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() - ordered_quantity);
            orderItem.setFulfilledQuantity(orderItem.getFulfilledQuantity() + ordered_quantity);
            orderItemApi.updateOrderItem(orderItem.getId(), orderItem);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<ChannelData> getAllChannels() {
        return channelDataApi.getAllChannel();
    }


    public void generateInvoice(Long id) throws TransformerException, ParserConfigurationException, IOException, FOPException {
        Order order = orderApi.get(id);
        ChannelData channelData = channelDataApi.getChannelDetails(order.getChannelId());
        if(channelData.getInvoiceType().equals(InvoiceType.SELF.toString())){
            InvoiceData invoiceData = getInvoiceData(id);
            PDFHandler.generatePDF(invoiceData, PDF_PATH, INVOICE_TEMPLATE_XSL);
            return;
        }
        channelDataApi.generateInvoice(id);
    }

    public byte[] downloadInvoice(Long id) throws IOException{
        byte[] fileInBytes;
        File file = new File(String.valueOf(Paths.get(PDF_PATH+"order"+id+".pdf")));
        // if file is not in assure then it will be in channel-app
        if(!(file.exists() && file.isFile())) {
            byte[] channelByteResponse = channelDataApi.getPDFBytes(id);
            if(channelByteResponse == null) {
                throw new ApiException("Invoice Pdf not found for orderId : "+ id);
            }
            logger.info("got pdf "+ channelByteResponse.length);
            return channelByteResponse;
        }
        else{
            fileInBytes = Files.readAllBytes(Paths.get(PDF_PATH+"order"+id+".pdf"));
            logger.info(fileInBytes.length);
            return Base64.getEncoder().encode(fileInBytes);
        }

    }


    public InvoiceData getInvoiceData(Long id) {
        Order order = orderApi.get(id);
        // create data
        List<OrderItem> orderItemList = orderItemApi.getOrderItemByOrderId(order.getId());
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setDate(getDate());
        invoiceData.setTime(getTime());
        invoiceData.setOrderId(order.getId());
        invoiceData.setClientName(clientApi.get(order.getClientId()).getName());
        invoiceData.setCustomerName(clientApi.get(order.getCustomerId()).getName());
        ChannelData channelData = channelDataApi.getChannelDetails(order.getChannelId());
        invoiceData.setChannelName(channelData.getName());
        invoiceData.setInvoiceType(channelData.getInvoiceType());
        Long index = 1L;
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            InvoiceItemData invoiceItemData = new InvoiceItemData();
            invoiceItemData.setId(index);
            invoiceItemData.setOrderedQuantity(orderItem.getOrderedQuantity());
            invoiceItemData.setSellingPricePerUnit(orderItem.getSellingPricePerUnit());
            Product product = productApi.get(orderItem.getGlobalSkuId());
            invoiceItemData.setProductName(product.getName());
            invoiceItemData.setClientSkuId(product.getClientSkuId());
            invoiceItemDataList.add(invoiceItemData);
            index++;
        }
        invoiceData.setInvoiceItemDataList(invoiceItemDataList);
        return invoiceData;
    }

    // Get date in required format
    private static String getDate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date dateObj = new Date();
        return df.format(dateObj);
    }

    // Get time in required format
    private static String getTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date timeObj = new Date();
        return df.format(timeObj);
    }


    public void setChannelRestTemplate(ChannelDataApi channelRestTemplate) {
        channelDataApi = channelRestTemplate;
    }
}
