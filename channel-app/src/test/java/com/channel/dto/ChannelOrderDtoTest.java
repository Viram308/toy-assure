package com.channel.dto;

import com.channel.api.ChannelListingApi;
import com.channel.assure.ClientAssure;
import com.channel.assure.OrderAssure;
import com.channel.assure.OrderItemAssure;
import com.channel.assure.ProductAssure;
import com.channel.model.form.ChannelForm;
import com.channel.model.response.ChannelOrderItemData;
import com.channel.pojo.ChannelListing;
import com.channel.spring.AbstractUnitTest;
import com.commons.api.CustomValidationException;
import com.commons.enums.ClientType;
import com.commons.enums.OrderStatus;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderItemForm;
import com.commons.form.OrderSearchForm;
import com.commons.response.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChannelOrderDtoTest extends AbstractUnitTest {

    private final OrderCsvForm orderCsvForm = new OrderCsvForm();
    private OrderItemForm orderItemForm1,orderItemForm2;
    private ProductData productData1, productData2;
    private ClientData clientData,customerData;
    private OrderData orderData;
    private ChannelForm channelForm1,channelForm2;
    private OrderSearchForm orderSearchForm;
    private OrderItemData orderItemData1,orderItemData2;
    private ChannelListing channelListing1,channelListing2;
    @Mock
    private ClientAssure clientAssure;
    @Mock
    private ProductAssure productAssure;
    @Mock
    private OrderAssure orderAssure;
    @Mock
    private OrderItemAssure orderItemAssure;
    @Mock
    private ChannelListingApi channelListingApi;
    @Autowired
    private ChannelDto channelDto;
    @Autowired
    private ChannelOrderDto channelOrderDto;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        channelForm1 = createChannelForm();
        channelForm2 = createChannelForm();
        orderItemForm1 = createOrderItemForm(10,15L);
        orderItemForm2 = createOrderItemForm(15,20L);
        clientData = createClientData(1L,"viram", ClientType.CLIENT.toString());
        customerData =createClientData(2L,"viram308",ClientType.CUSTOMER.toString());
        productData1 = createProductData("prod1", 1L, "munch", 10.50);
        productData2 = createProductData("prod2", 2L, "kitkat", 15);
        orderData = createOrderData();
        orderSearchForm = createOrderSearchForm();
        orderItemData1 = createOrderItemData(orderItemForm1,productData1);
        orderItemData2 = createOrderItemData(orderItemForm2,productData2);
        channelListing1 = createChannelListing(1L,1L);
        channelListing2 =createChannelListing(2L,2L);
    }

    private ChannelListing createChannelListing(Long id,Long globalSkuId) {
        ChannelListing channelListing = new ChannelListing();
        channelListing.setId(id);
        channelListing.setClientId(1L);
        channelListing.setGlobalSkuId(globalSkuId);
        return channelListing;
    }

    private OrderItemData createOrderItemData(OrderItemForm orderItemForm,ProductData productData) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
        orderItemData.setClientSkuId(orderItemForm.getClientSkuId());
        orderItemData.setOrderedQuantity(orderItemForm.getOrderedQuantity());
        orderItemData.setProductName(productData.getProductName());
        orderItemData.setBrandId(productData.getBrandId());
        return orderItemData;
    }

    private OrderSearchForm createOrderSearchForm() {
        OrderSearchForm orderSearchForm = new OrderSearchForm();
        orderSearchForm.setChannelId(0L);
        orderSearchForm.setOrderStatus("");
        orderSearchForm.setClientId(0L);
        return orderSearchForm;
    }

    private ChannelForm createChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName("channel");
        channelForm.setInvoiceType("CHANNEL");
        return channelForm;
    }

    private OrderData createOrderData() {
        OrderData orderData = new OrderData();
        orderData.setClientId(1L);
        orderData.setChannelName("channel");
        orderData.setChannelOrderId("o1");
        orderData.setClientName(clientData.getName());
        orderData.setCustomerName(customerData.getName());
        orderData.setOrderId(1L);
        orderData.setStatus(OrderStatus.CREATED.toString());
        return orderData;
    }

    private ClientData createClientData(Long id,String name,String type) {
        ClientData clientData = new ClientData();
        clientData.setId(id);
        clientData.setName(name);
        clientData.setType(type);
        return clientData;
    }

    private ProductData createProductData(String clientSkuId, Long globalSkuId, String name, double mrp) {
        ProductData productData = new ProductData();
        productData.setClientSkuId(clientSkuId);
        productData.setGlobalSkuId(globalSkuId);
        productData.setProductName(name);
        productData.setClientName(clientData.getName());
        productData.setBrandId("nestle");
        productData.setMrp(mrp);
        return productData;
    }

    private OrderItemForm createOrderItemForm(double sellingPricePerUnit,Long orderedQuantity) {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setSellingPricePerUnit(sellingPricePerUnit);
        orderItemForm.setOrderedQuantity(orderedQuantity);
        orderItemForm.setClientId(1L);
        orderItemForm.setCustomerId(2L);
        orderItemForm.setClientSkuId("prod1");
        orderItemForm.setChannelOrderId("o1");
        return orderItemForm;
    }

    private void addDetails(){
        // add channel
        ChannelData channelData = channelDto.addChannel(channelForm1);
        // set channel
        orderItemForm1.setChannelId(channelData.getId());
        orderItemForm2.setChannelId(channelData.getId());
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderCsvForm.setOrderItemFormList(orderItemFormList);
        // set restTemplate
        channelOrderDto.setClientAssureRestTemplate(clientAssure);
        channelOrderDto.setProductAssureRestTemplate(productAssure);
        channelOrderDto.setOrderAssureRestTemplate(orderAssure);
        channelOrderDto.setOrderItemAssureRestTemplate(orderItemAssure);
        channelOrderDto.setChannelListingApiRestTemplate(channelListingApi);
        List<ProductData> productDataList = new ArrayList<>();
        productDataList.add(productData1);
        productDataList.add(productData2);
        orderData.setChannelId(channelData.getId());
        List<OrderData> orderDataList = new ArrayList<>();
        orderDataList.add(orderData);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        orderItemDataList.add(orderItemData1);
        orderItemDataList.add(orderItemData2);
        // set listing data
        channelListing1.setChannelId(channelData.getId());
        channelListing1.setChannelSkuId("channelSku1");

        channelListing2.setChannelId(channelData.getId());
        channelListing2.setChannelSkuId("channelSku2");
        // rules
        when(clientAssure.getClientData(1L)).thenReturn(clientData);
        when(clientAssure.getClientData(2L)).thenReturn(customerData);
        when(productAssure.getProductByClientIdAndClientSkuId(1L)).thenReturn(productDataList);
        when(orderAssure.getOrderDetails("o1",1L)).thenReturn(orderData);
        when(orderAssure.getChannelOrders()).thenReturn(orderDataList);
        when(orderAssure.addChannelOrder(orderCsvForm)).thenReturn("Success");
        when(orderAssure.searchChannelOrder(orderSearchForm)).thenReturn(orderDataList);
        when(orderAssure.get(1L)).thenReturn(orderData);
        when(orderItemAssure.getOrderItems(1L)).thenReturn(orderItemDataList);
        when(channelListingApi.getChannelListingByParameters(channelData.getId(),1L,1L)).thenReturn(channelListing1);
        when(channelListingApi.getChannelListingByParameters(channelData.getId(),1L,2L)).thenReturn(channelListing2);
    }

    // test for adding channel order
    @Test(expected = CustomValidationException.class)
    public void testAddChannelOrder(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        String response = channelOrderDto.addChannelOrder(orderCsvForm,result);
        assertEquals("Success",response);
        when(result.hasErrors()).thenReturn(true);
        channelOrderDto.addChannelOrder(orderCsvForm,result);

    }

    // test for getting channel order list
    @Test
    public void testGetOrderList(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelOrderDto.addChannelOrder(orderCsvForm,result);
        List<OrderData> orderDataList = channelOrderDto.getChannelOrders();
        assertEquals(1,orderDataList.size());
    }

    // test for searching channel order
    @Test
    public void testSearchChannelOrder(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelOrderDto.addChannelOrder(orderCsvForm,result);
        List<OrderData> orderDataList = channelOrderDto.searchChannelOrders(orderSearchForm);
        assertEquals(1,orderDataList.size());
    }

    // test for getting order items
    @Test
    public void testGetOrderItems(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelOrderDto.addChannelOrder(orderCsvForm,result);
        List<ChannelOrderItemData> orderItemDataList = channelOrderDto.getOrderItems(1L);
        assertEquals(2,orderItemDataList.size());
    }

    // test for getting invoice data
    @Test
    public void testInvoiceData(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        channelOrderDto.addChannelOrder(orderCsvForm,result);
        InvoiceData invoiceData = channelOrderDto.getInvoiceData(1L);
        assertNotNull(invoiceData);
        assertEquals(2,invoiceData.getInvoiceItemDataList().size());
    }

    // test for getting order data except internal channel
    @Test
    public void testGetOrderDataExceptInternalChannel(){
        channelForm2.setChannelName("internal");
        channelForm2.setInvoiceType("SELF");
        ChannelData channelData = channelDto.addChannel(channelForm2);
        // set order data
        orderData.setChannelId(channelData.getId());
        List<OrderData> orderDataList = new ArrayList<>();
        // update list
        orderDataList.add(orderData);
        assertEquals(1,orderDataList.size());
        orderDataList = channelOrderDto.getOrderDataExceptInternalChannel(orderDataList);
        assertEquals(0,orderDataList.size());
    }

}
