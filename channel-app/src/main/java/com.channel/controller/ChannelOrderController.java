package com.channel.controller;

import com.channel.dto.ChannelOrderDto;
import com.channel.model.response.ChannelOrderItemData;
import com.commons.form.OrderCsvForm;
import com.commons.form.OrderSearchForm;
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
@RequestMapping(value = "/api/channelOrder")
public class ChannelOrderController {

    private static final Logger logger = Logger.getLogger(ChannelOrderController.class);

    @Autowired
    private ChannelOrderDto channelOrderDto;

    @ApiOperation(value = "Adds a Channel Order")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addChannelOrder(@RequestBody OrderCsvForm orderCsvForm, BindingResult result){
        logger.info("adding channel order");
        channelOrderDto.addChannelOrder(orderCsvForm,result);
        return "Channel order added";
    }

    @ApiOperation(value = "Get Channel Orders")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderData> getChannelOrders(){
        logger.info("getting channel orders");
        return channelOrderDto.getChannelOrders();
    }

    @ApiOperation(value = "Search Channel Orders")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<OrderData> searchChannelOrders(@RequestBody OrderSearchForm orderSearchForm){
        logger.info("search channel orders");
        return channelOrderDto.searchChannelOrders(orderSearchForm);
    }

    @ApiOperation(value = "Gets order items by orderId")
    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public List<ChannelOrderItemData> getOrderItemsByOrderId(@PathVariable Long id) {
        logger.info("get all order items for orderId");
        return channelOrderDto.getOrderItems(id);
    }

    @ApiOperation(value = "Download invoice")
    @RequestMapping(value = "/getPDFBytes/{id}", method = RequestMethod.GET)
    public byte[] getPDFBytes(@PathVariable Long id) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        logger.info("get pdf bytes");
        return channelOrderDto.getPDFBytes(id);
    }

    @ApiOperation(value = "Download invoice")
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void downloadInvoice(@PathVariable Long id, HttpServletResponse response) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        logger.info("Download Order");
        channelOrderDto.downloadInvoice(id,response);
    }

}
