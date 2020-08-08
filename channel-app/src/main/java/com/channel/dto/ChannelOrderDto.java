package com.channel.dto;

import com.channel.api.ChannelApi;
import com.channel.api.ChannelListingApi;
import com.channel.assure.ClientAssure;
import com.channel.assure.OrderAssure;
import com.channel.assure.OrderItemAssure;
import com.channel.assure.ProductAssure;
import com.channel.model.response.ChannelOrderItemData;
import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.channel.validator.ChannelOrderCsvFormValidator;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import com.commons.enums.InvoiceType;
import com.commons.form.OrderCsvForm;
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
public class ChannelOrderDto {

    private static final Logger logger = Logger.getLogger(ChannelOrderDto.class);
    private static final String PDF_PATH = "src/main/resources/com/channel/";
    private static final String INVOICE_TEMPLATE_XSL = "src/main/resources/com/channel/invoiceTemplate.xsl";

    @Autowired
    private ClientAssure clientAssure;
    @Autowired
    private ChannelApi channelApi;
    @Autowired
    private OrderAssure orderAssure;
    @Autowired
    private OrderItemAssure orderItemAssure;
    @Autowired
    private ProductAssure productAssure;
    @Autowired
    private ChannelListingApi channelListingApi;
    @Autowired
    private ChannelOrderCsvFormValidator channelOrderCsvFormValidator;

    @Transactional(rollbackFor = CustomValidationException.class)
    public String addChannelOrder(OrderCsvForm orderCsvForm, BindingResult result) {
        channelOrderCsvFormValidator.validate(orderCsvForm, result);
        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }
        return orderAssure.addChannelOrder(orderCsvForm);
    }

    @Transactional(readOnly = true)
    public List<OrderData> getChannelOrders() {
        List<OrderData> orderDataList = orderAssure.getChannelOrders();
        return getOrderDataExceptInternalChannel(orderDataList);
    }

    @Transactional(readOnly = true)
    public List<OrderData> searchChannelOrders(OrderSearchForm orderSearchForm) {
        List<OrderData> orderDataList = orderAssure.searchChannelOrder(orderSearchForm);
        return getOrderDataExceptInternalChannel(orderDataList);
    }

    @Transactional(readOnly = true)
    public List<ChannelOrderItemData> getOrderItems(Long id) {
        OrderData orderData = orderAssure.get(id);
        List<OrderItemData> orderItemDataList = orderItemAssure.getOrderItems(id);
        return convertToChannelOrderItemData(orderItemDataList, orderData.getClientId(), orderData.getChannelId());
    }

    private List<ChannelOrderItemData> convertToChannelOrderItemData(List<OrderItemData> orderItemDataList, Long clientId, Long channelId) {
        List<ChannelOrderItemData> channelOrderItemDataList = new ArrayList<>();
        for (OrderItemData orderItemData : orderItemDataList) {
            ChannelOrderItemData channelOrderItemData = new ChannelOrderItemData();
            List<ProductData> productDataList = productAssure.getProductByClientIdAndClientSkuId(clientId);
            productDataList = productDataList.stream().filter(productData -> (StringUtil.toLowerCase(productData.getClientSkuId()).equals(StringUtil.toLowerCase(orderItemData.getClientSkuId())))).collect(Collectors.toList());
            ChannelListing channelListing = channelListingApi.getChannelListingByParameters(channelId, clientId, productDataList.get(0).getGlobalSkuId());
            channelOrderItemData.setChannelSkuId(channelListing.getChannelSkuId());
            channelOrderItemData.setBrandId(orderItemData.getBrandId());
            channelOrderItemData.setClientSkuId(orderItemData.getClientSkuId());
            channelOrderItemData.setOrderedQuantity(orderItemData.getOrderedQuantity());
            channelOrderItemData.setProductName(orderItemData.getProductName());
            channelOrderItemData.setSellingPricePerUnit(orderItemData.getSellingPricePerUnit());
            channelOrderItemDataList.add(channelOrderItemData);
        }
        return channelOrderItemDataList;
    }

    public List<OrderData> getOrderDataExceptInternalChannel(List<OrderData> orderDataList){
        List<OrderData> orderDataList1 = new ArrayList<>();
        for(OrderData orderData : orderDataList){
            Channel channel = channelApi.get(orderData.getChannelId());
            if(!(channel.getName().equals("internal") && channel.getInvoiceType().equals(InvoiceType.SELF))){
                orderDataList1.add(orderData);
            }
        }
        return orderDataList1;
    }


    public void generateInvoice(Long id) throws TransformerException, ParserConfigurationException, IOException, FOPException {
        logger.info("Generating Invoice in channel");
        InvoiceData invoiceData = getInvoiceData(id);
        logger.info("Generating PDF");
        PDFHandler.generatePDF(invoiceData, PDF_PATH, INVOICE_TEMPLATE_XSL);
        logger.info("PDF generated");
    }

    public InvoiceData getInvoiceData(Long id) {
        OrderData order = orderAssure.get(id);
        List<OrderItemData> orderItemDataList = orderItemAssure.getOrderItems(id);
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setDate(getDate());
        invoiceData.setTime(getTime());
        invoiceData.setOrderId(id);
        invoiceData.setClientName(order.getClientName());
        invoiceData.setCustomerName(order.getCustomerName());
        invoiceData.setChannelName(order.getChannelName());
        invoiceData.setInvoiceType(InvoiceType.CHANNEL.toString());
        Long index = 1L;
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<>();
        for (OrderItemData orderItemData : orderItemDataList) {
            InvoiceItemData invoiceItemData = new InvoiceItemData();
            invoiceItemData.setId(index);
            invoiceItemData.setOrderedQuantity(orderItemData.getOrderedQuantity());
            invoiceItemData.setSellingPricePerUnit(orderItemData.getSellingPricePerUnit());
            invoiceItemData.setProductName(orderItemData.getProductName());
            invoiceItemData.setBrandId(orderItemData.getBrandId());
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

    public byte[] downloadInvoice(Long id) throws IOException {
        byte[] fileBytes;
        File file = new File(String.valueOf(Paths.get(PDF_PATH+"order"+id+".pdf")));
        if(!(file.exists() && file.isFile())) {
            byte[] assureByteResponse = orderAssure.getPDFBytes(id);
            if(assureByteResponse == null) {
                throw new ApiException("Invoice Pdf not found for orderId : "+ id);
            }
            logger.info("got pdf "+ assureByteResponse.length);
            return assureByteResponse;
        }
        else {
            fileBytes = Files.readAllBytes(Paths.get(PDF_PATH + "order" + id + ".pdf"));
            logger.info(fileBytes.length);
            return Base64.getEncoder().encode(fileBytes);
        }
    }

    public ClientData getClientData(Long clientId) {
        return clientAssure.getClientData(clientId);
    }

    public OrderData getOrderDetails(String channelOrderId, Long channelId) {
        return orderAssure.getOrderDetails(channelOrderId,channelId);
    }

    public List<ProductData> getProductByClientIdAndClientSkuId(Long clientId) {
        return productAssure.getProductByClientIdAndClientSkuId(clientId);
    }

    public void setClientAssureRestTemplate(ClientAssure clientAssureRestTemplate) {
        clientAssure = clientAssureRestTemplate;
    }

    public void setProductAssureRestTemplate(ProductAssure productAssureRestTemplate) {
        productAssure = productAssureRestTemplate;
    }

    public void setOrderAssureRestTemplate(OrderAssure orderAssureRestTemplate){
        orderAssure = orderAssureRestTemplate;
    }

    public void setOrderItemAssureRestTemplate(OrderItemAssure orderItemAssureRestTemplate) {
        orderItemAssure = orderItemAssureRestTemplate;
    }

    public void setChannelListingApiRestTemplate(ChannelListingApi channelListingApiRestTemplate) {
        channelListingApi = channelListingApiRestTemplate;
    }
}
