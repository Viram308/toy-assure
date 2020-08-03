package com.channel.dto;

import com.channel.api.ChannelListingApi;
import com.channel.assure.OrderAssure;
import com.channel.assure.OrderItemAssure;
import com.channel.assure.ProductAssure;
import com.channel.model.response.ChannelOrderItemData;
import com.channel.pojo.ChannelListing;
import com.channel.validator.ChannelOrderCsvFormValidator;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    public void addChannelOrder(OrderCsvForm orderCsvForm, BindingResult result) {
        channelOrderCsvFormValidator.validate(orderCsvForm, result);
        logger.info("Errors in form :" + result.getErrorCount());
        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }
        String response = orderAssure.addChannelOrder(orderCsvForm);
        logger.info(response);
    }

    @Transactional(readOnly = true)
    public List<OrderData> getChannelOrders() {
        List<OrderData> orderDataList = orderAssure.getChannelOrders();
        return orderDataList.stream().filter(orderData -> !(orderData.getChannelId().equals(1L))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderData> searchChannelOrders(OrderSearchForm orderSearchForm) {
        return orderAssure.searchChannelOrder(orderSearchForm);
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

    public byte[] downloadInvoice(Long id) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(PDF_PATH+"order"+id+".pdf"));
        return Base64.getEncoder().encode(fileBytes);
    }
}
