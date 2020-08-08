package com.assure.controller;

import com.assure.dto.OrderDto;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderSearchForm;
import com.commons.response.ChannelData;
import com.commons.response.OrderData;
import com.commons.response.OrderItemData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderApiController {
    @Autowired
    private OrderDto orderDto;

    private static final Logger logger = Logger.getLogger(OrderApiController.class);

    @ApiOperation(value = "Adds a new order")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<OrderData> addOrderDetail(@RequestBody OrderCsvForm orderCsvForm, BindingResult result) {
        logger.info("add-new-order");
        return orderDto.addOrder(orderCsvForm, result);
    }

    @ApiOperation(value = "Adds a new channel order")
    @RequestMapping(value = "/addChannelOrder", method = RequestMethod.POST)
    public void addChannelOrderDetail(@RequestBody OrderCsvForm orderCsvForm, BindingResult result) {
        logger.info("add-new-channel-order");
        orderDto.addChannelOrder(orderCsvForm);
    }

    @ApiOperation(value = "Search order")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<OrderData> searchDetail(@RequestBody OrderSearchForm orderSearchForm) {
        logger.info("search-order");
        return orderDto.searchOrder(orderSearchForm);
    }

    @ApiOperation(value = "Search Channel order")
    @RequestMapping(value = "/searchChannelOrder", method = RequestMethod.POST)
    public List<OrderData> searchChannelOrder(@RequestBody OrderSearchForm orderSearchForm) {
        logger.info("search-channel-order");
        return orderDto.searchChannelOrder(orderSearchForm);
    }

    @ApiOperation(value = "Gets all order")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderData> getAllOrder() {
        logger.info("get all orders");
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Gets all order")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public OrderData getAllOrder(@PathVariable Long id) {
        logger.info("get by order id");
        return orderDto.get(id);
    }

    @ApiOperation(value = "Gets order by channelId and channelOrderId")
    @RequestMapping(value = "/{channelId}/{channelOrderId}", method = RequestMethod.GET)
    public OrderData getAllOrder(@PathVariable Long channelId,@PathVariable String channelOrderId) {
        logger.info("Gets order by channelId and channelOrderId");
        return orderDto.getOrderDetails(channelOrderId,channelId);
    }

    @ApiOperation(value = "Gets order items by orderId")
    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItemsByOrderId(@PathVariable Long id) {
        logger.info("get all order items for orderId");
        return orderDto.getOrderItems(id);
    }

    @ApiOperation(value = "Fulfill Order")
    @RequestMapping(value = "/fulfill/{id}", method = RequestMethod.GET)
    public void fulfillOrder(@PathVariable Long id) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        logger.info("Fulfill Order");
        orderDto.fulfillOrder(id);
        orderDto.generateInvoice(id);
    }

    @ApiOperation(value = "Allocate Order")
    @RequestMapping(value = "/allocate/{id}", method = RequestMethod.GET)
    public List<OrderData> allocateOrder(@PathVariable Long id) {
        logger.info("Allocate Orders");
        return orderDto.allocateOrder(id);
    }

    @ApiOperation(value = "Get all channels")
    @RequestMapping(value = "/allChannel", method = RequestMethod.GET)
    public List<ChannelData> getAllChannel() {
        logger.info("get channels");
        return orderDto.getAllChannels();
    }

    @ApiOperation(value = "Download invoice")
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public byte[] downloadInvoice(@PathVariable Long id, HttpServletResponse response) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        logger.info("Download Order");
        return orderDto.downloadInvoice(id,response);
    }
}
