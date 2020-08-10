package com.assure.dto;

import com.assure.channel.ChannelDataApi;
import com.assure.model.form.BinForm;
import com.assure.model.form.BinSkuCsvForm;
import com.assure.model.form.BinSkuForm;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.CustomValidationException;
import com.commons.enums.ClientType;
import com.commons.enums.OrderStatus;
import com.commons.form.*;
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

public class OrderDtoTest extends AbstractUnitTest {
    private ClientForm clientForm1,clientForm2;
    private ProductForm productForm1,productForm2,productForm3;
    private BinForm binForm1,binForm2;
    private ProductCsvForm productCsvForm = new ProductCsvForm();
    private BinSkuCsvForm binSkuCsvForm = new BinSkuCsvForm();
    private BinSkuForm binSkuForm1,binSkuForm2;
    private OrderCsvForm orderCsvForm = new OrderCsvForm();
    private OrderItemForm orderItemForm1,orderItemForm2;
    private ChannelData channelData;
    private List<ChannelData> channelDataList = new ArrayList<>();
    private OrderSearchForm orderSearchForm;

    @Mock
    private ChannelDataApi channelDataApi;
    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BinDto binDto;
    @Autowired
    private BinSkuDto binSkuDto;
    @Autowired
    private OrderDto orderDto;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        clientForm1 = createClientForm("viram", ClientType.CLIENT);
        clientForm2 = createClientForm("viram308",ClientType.CUSTOMER);
        productForm1 = createProductForm("munch",10.00,"excellent","prod1","nestle");
        productForm2 = createProductForm("kitkat",15.00,"nice","prod2","britannia");
        productForm3 = createProductForm("munch",10.50,"nice","prod1","nestle");
        binForm1 = createBinForm();
        binForm2 = createBinForm();
        binSkuForm1 = createBinSkuForm(20L);
        binSkuForm2 = createBinSkuForm(30L);
        orderItemForm1 = createOrderItemForm(10,15L);
        orderItemForm2 = createOrderItemForm(15,20L);
        channelData = createChannelData();
        orderSearchForm = createOrderSearchForm();
    }

    private OrderSearchForm createOrderSearchForm() {
        OrderSearchForm orderSearchForm = new OrderSearchForm();
        orderSearchForm.setChannelId(channelData.getId());
        orderSearchForm.setOrderStatus("");
        orderSearchForm.setClientId(0L);
        return orderSearchForm;
    }

    private ChannelData createChannelData() {
        ChannelData channelData = new ChannelData();
        channelData.setId(1L);
        channelData.setName("internal");
        channelData.setInvoiceType("SELF");
        return channelData;
    }

    private OrderItemForm createOrderItemForm(double sellingPricePerUnit,Long orderedQuantity) {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setSellingPricePerUnit(sellingPricePerUnit);
        orderItemForm.setOrderedQuantity(orderedQuantity);
        return orderItemForm;
    }

    private BinSkuForm createBinSkuForm(Long quantity) {
        BinSkuForm binSkuForm = new BinSkuForm();
        binSkuForm.setQuantity(quantity);
        return binSkuForm;
    }

    private BinForm createBinForm(){
        BinForm binForm = new BinForm();
        binForm.setNoOfBins(1L);
        return binForm;
    }
    private ProductForm createProductForm(String productName,double mrp,String description,String clientSkuId,String brandId) {
        ProductForm productForm = new ProductForm();
        productForm.setProductName(productName);
        productForm.setMrp(mrp);
        productForm.setDescription(description);
        productForm.setClientSkuId(clientSkuId);
        productForm.setBrandId(brandId);
        return productForm;
    }

    private ClientForm createClientForm(String name, ClientType type) {
        ClientForm clientForm = new ClientForm();
        clientForm.setName(name);
        clientForm.setType(type);
        return clientForm;
    }

    private void addDetails(){
        ClientData clientData1 = clientDto.addClient(clientForm1);
        ClientData clientData2 = clientDto.addClient(clientForm2);
        // set client id
        productForm1.setClientId(clientData1.getId());
        productForm2.setClientId(clientData1.getId());
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm1);
        productFormList.add(productForm2);
        productCsvForm.setProductFormList(productFormList);
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // add products
        productDto.addProducts(productCsvForm,result);
        // add bins
        List<Long> binIdList = binDto.addBins(binForm1);
        // set binSkuData
        binSkuForm1.setClientId(clientData1.getId());
        binSkuForm1.setClientSkuId(productForm1.getClientSkuId());
        binSkuForm1.setBinId(binIdList.get(0));
        binSkuForm2.setClientId(clientData1.getId());
        binSkuForm2.setClientSkuId(productForm2.getClientSkuId());
        binSkuForm2.setBinId(binIdList.get(0));
        List<BinSkuForm> binSkuFormList = new ArrayList<>();
        binSkuFormList.add(binSkuForm1);
        binSkuFormList.add(binSkuForm2);
        binSkuCsvForm.setBinSkuFormList(binSkuFormList);
        BindingResult result1 = mock(BindingResult.class);
        when(result1.hasErrors()).thenReturn(false);
        // add binSku
        binSkuDto.addBinSku(binSkuCsvForm,result1);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        // set line items data
        orderItemForm1.setClientId(clientData1.getId());
        orderItemForm1.setCustomerId(clientData2.getId());
        orderItemForm1.setClientSkuId(productForm1.getClientSkuId());
        orderItemForm1.setChannelOrderId("o1");
        orderItemForm1.setChannelId(1L);
        orderItemForm2.setClientId(clientData1.getId());
        orderItemForm2.setCustomerId(clientData2.getId());
        orderItemForm2.setClientSkuId(productForm2.getClientSkuId());
        orderItemForm2.setChannelOrderId("o1");
        orderItemForm2.setChannelId(1L);
        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderCsvForm.setOrderItemFormList(orderItemFormList);
        channelDataList.add(channelData);
    }

    // test for add order
    @Test(expected = CustomValidationException.class)
    public void testAddOrder(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        List<OrderData> orderDataList = orderDto.addOrder(orderCsvForm,result2);
        assertEquals(1,orderDataList.size());
        when(result2.hasErrors()).thenReturn(true);
        // throw exception
        orderDto.addOrder(orderCsvForm,result2);
    }

    // test for adding channel order
    @Test
    public void testAddChannelOrder(){
        addDetails();
        // set restTemplate
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        orderDto.addChannelOrder(orderCsvForm);
        List<OrderData> orderDataList = orderDto.getAllOrders();
        assertEquals(1,orderDataList.size());
    }

    // test for getting orderDetails by channelOrderId and channelId
    @Test
    public void testGetOrderDetails(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        // set restTemplate
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        // add order
        orderDto.addOrder(orderCsvForm,result2);
        OrderData orderData = orderDto.getOrderDetails(orderCsvForm.getOrderItemFormList().get(0).getChannelOrderId(),channelData.getId());
        // quantity is available so order is allocated
        assertEquals(OrderStatus.ALLOCATED.toString(),orderData.getStatus());
    }

    // test for getting order by orderId
    @Test
    public void testGet(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        // set restTemplate
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        List<OrderData> orderDataList = orderDto.addOrder(orderCsvForm,result2);
        // get order
        OrderData orderData = orderDto.get(orderDataList.get(0).getOrderId());
        assertEquals(OrderStatus.ALLOCATED.toString(),orderData.getStatus());
    }

    // test for getting all orders
    @Test
    public void testGetAll(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        // set restTemplate
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        orderDto.addOrder(orderCsvForm,result2);
        // get all
        List<OrderData> orderDataList = orderDto.getAllOrders();
        assertEquals(1,orderDataList.size());
    }

    // test for search order
    @Test
    public void testSearchOrder(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        // set restTemplate
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        // add order
        orderDto.addOrder(orderCsvForm,result2);
        List<OrderData> orderDataList = orderDto.searchOrder(orderSearchForm);
        assertEquals(1,orderDataList.size());
        // update search form
        orderSearchForm.setOrderStatus(OrderStatus.CREATED.toString());
        orderDataList = orderDto.searchOrder(orderSearchForm);
        assertEquals(0,orderDataList.size());
        // update search form
        orderSearchForm.setOrderStatus(OrderStatus.ALLOCATED.toString());
        orderSearchForm.setClientId(productForm1.getClientId()+1);
        orderDataList = orderDto.searchOrder(orderSearchForm);
        assertEquals(0,orderDataList.size());
    }

    // test for getting orderItems by orderId
    @Test
    public void testGetOrderItems(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        List<OrderData> orderDataList = orderDto.addOrder(orderCsvForm,result2);
        List<OrderItemData> orderItemDataList = orderDto.getOrderItems(orderDataList.get(0).getOrderId());
        // test data
        assertEquals(2,orderItemDataList.size());
        assertEquals(orderItemForm1.getOrderedQuantity(),orderItemDataList.get(0).getOrderedQuantity());
        assertEquals(orderItemForm1.getSellingPricePerUnit(),orderItemDataList.get(0).getSellingPricePerUnit(),0.01);
        assertEquals(orderItemForm1.getClientSkuId(),orderItemDataList.get(0).getClientSkuId());
        assertEquals(orderItemForm2.getOrderedQuantity(),orderItemDataList.get(1).getOrderedQuantity());
        assertEquals(orderItemForm2.getSellingPricePerUnit(),orderItemDataList.get(1).getSellingPricePerUnit(),0.01);
        assertEquals(orderItemForm2.getClientSkuId(),orderItemDataList.get(1).getClientSkuId());
    }

    // test for fulfill order
    @Test
    public void testFulFillOrder(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        List<OrderData> orderDataList = orderDto.addOrder(orderCsvForm,result2);
        orderDto.fulfillOrder(orderDataList.get(0).getOrderId());
        OrderData orderData = orderDto.get(orderDataList.get(0).getOrderId());
        // test fulfillment
        assertEquals(OrderStatus.FULFILLED.toString(),orderData.getStatus());
    }

    // test for getting all channel
    @Test
    public void testGetAllChannel(){
        addDetails();
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getAllChannel()).thenReturn(channelDataList);
        List<ChannelData> channelDataList = orderDto.getAllChannels();
        assertEquals(1,channelDataList.size());
    }

    // test for getting invoice data
    @Test
    public void testGetInvoiceData(){
        addDetails();
        BindingResult result2 = mock(BindingResult.class);
        when(result2.hasErrors()).thenReturn(false);
        orderDto.setChannelRestTemplate(channelDataApi);
        when(channelDataApi.getChannelDetails(1L)).thenReturn(channelData);
        List<OrderData> orderDataList = orderDto.addOrder(orderCsvForm,result2);
        InvoiceData invoiceData = orderDto.getInvoiceData(orderDataList.get(0).getOrderId());
        assertNotNull(invoiceData);
    }

}
