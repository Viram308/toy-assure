package com.assure.controller;

import com.assure.dto.OrderDto;
import com.assure.model.form.OrderCsvForm;
import com.assure.model.form.OrderSearchForm;
import com.assure.model.response.OrderData;
import com.assure.model.response.OrderItemData;
import com.commons.response.ChannelData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation(value = "Search order")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<OrderData> searchDetail(@RequestBody OrderSearchForm orderSearchForm) {
        logger.info("search-order");
        return orderDto.searchOrder(orderSearchForm);
    }

    @ApiOperation(value = "Gets all order")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderData> getAllOrder() {
        logger.info("get all orders");
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Gets order items by orderId")
    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItemsByOrderId(@PathVariable Long id) {
        logger.info("get all order items for orderId");
        return orderDto.getOrderItems(id);
    }

    @ApiOperation(value = "Fulfill Order")
    @RequestMapping(value = "/fulfill/{id}", method = RequestMethod.GET)
    public void fulfillOrder(@PathVariable Long id,HttpServletResponse response) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        logger.info("Fulfill Order");
        orderDto.fulfillOrder(id,response);
    }

    @ApiOperation(value = "Allocate Order")
    @RequestMapping(value = "/allocate", method = RequestMethod.GET)
    public List<OrderData> allocateOrder() {
        logger.info("Allocate Orders");
        return orderDto.allocateOrders();
    }

    @ApiOperation(value = "Get all channels")
    @RequestMapping(value = "/allChannel", method = RequestMethod.GET)
    public List<ChannelData> getAllChannel() {
        logger.info("get channels");
        return orderDto.getAllChannels();
    }

    @ApiOperation(value = "Download invoice")
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void downloadInvoice(@PathVariable Long id, HttpServletResponse response) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        logger.info("Download Order");
        orderDto.downloadInvoice(id,response);
    }
}
