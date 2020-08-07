package com.channel.validator;

import com.channel.assure.ClientAssure;
import com.channel.assure.OrderAssure;
import com.channel.assure.ProductAssure;
import com.channel.dto.ChannelDto;
import com.channel.dto.ChannelOrderDto;
import com.channel.model.form.ChannelForm;
import com.channel.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.enums.OrderStatus;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderItemForm;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import com.commons.response.OrderData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ChannelOrderCsvFormValidatorTest extends AbstractUnitTest {
    private ChannelForm channelForm1;
    private OrderItemForm orderItemForm1,orderItemForm2;
    private final OrderCsvForm orderCsvForm = new OrderCsvForm();
    private ClientData clientData,customerData;
    private OrderData orderData;

    @Mock
    private ClientAssure clientAssure;
    @Mock
    private ProductAssure productAssure;
    @Mock
    private OrderAssure orderAssure;
    @Autowired
    private ChannelDto channelDto;
    @Autowired
    private ChannelOrderDto channelOrderDto;
    @Autowired
    private ChannelOrderCsvFormValidator channelOrderCsvFormValidator;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        channelForm1 = createChannelForm();
        clientData = createClientData(1L,"viram", ClientType.CLIENT.toString());
        customerData =createClientData(2L,"viram308",ClientType.CUSTOMER.toString());
        orderData = createOrderData();
        orderItemForm1 = createOrderItemForm(10,15L);
        orderItemForm2 = createOrderItemForm(15,20L);



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

    private ChannelForm createChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName("channel");
        channelForm.setInvoiceType("CHANNEL");
        return channelForm;
    }

    private void addDetails(){
        ChannelData channelData = channelDto.addChannel(channelForm1);
        orderItemForm1.setChannelId(channelData.getId());
        orderItemForm2.setChannelId(channelData.getId());

        channelOrderDto.setClientAssureRestTemplate(clientAssure);
        channelOrderDto.setProductAssureRestTemplate(productAssure);
        channelOrderDto.setOrderAssureRestTemplate(orderAssure);
        orderData.setChannelId(channelData.getId());
        when(clientAssure.getClientData(0L)).thenReturn(null);
        when(productAssure.getProductByClientIdAndClientSkuId(1L)).thenReturn(new ArrayList<>());
        when(orderAssure.getOrderDetails("o1",channelData.getId())).thenReturn(null);

    }

    @Test
    public void testValidate(){
        addDetails();
        orderItemForm1.setClientId(0L);
        orderItemForm1.setCustomerId(0L);
        orderItemForm1.setChannelOrderId("o1");
        orderItemForm2.setClientId(0L);
        orderItemForm2.setCustomerId(0L);
        orderItemForm2.setChannelOrderId("o1");
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderCsvForm.setOrderItemFormList(orderItemFormList);
        Errors errors = new BeanPropertyBindingResult(orderCsvForm,"Invalid Csv");
        channelOrderCsvFormValidator.validate(orderCsvForm,errors);
        assertTrue(errors.hasErrors());
        assertEquals(6,errors.getErrorCount());
    }
}
