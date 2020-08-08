package com.channel.controller;

import com.channel.dto.ChannelDto;
import com.channel.model.form.ChannelForm;
import com.commons.response.ChannelData;
import com.commons.response.ClientData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/channel")
public class ChannelApiController {

    private static final Logger logger = Logger.getLogger(ChannelApiController.class);

    @Autowired
    private ChannelDto channelDto;
    // CRUD operations for channel

    @ApiOperation(value = "Adds a Channel")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ChannelData addChannel(@RequestBody ChannelForm channelForm) throws Exception {
        logger.info("adding channel");
        return channelDto.addChannel(channelForm);
    }

    @ApiOperation(value = "Gets a Channel")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ChannelData getChannel(@PathVariable Long id) {
        logger.info("get channel for id : " + id);
        return channelDto.getChannel(id);
    }

    @ApiOperation(value = "Gets Channels by ClientId")
    @RequestMapping(value = "/getByClient/{clientId}", method = RequestMethod.GET)
    public List<ChannelData> getChannelByClient(@PathVariable Long clientId) {
        logger.info("get channel for id : " + clientId);
        return channelDto.getChannelByClient(clientId);
    }

    @ApiOperation(value = "Search Channels")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<ChannelData> search(@RequestBody ChannelForm channelForm) {
        logger.info("search channels");
        return channelDto.searchChannels(channelForm);
    }

    @ApiOperation(value = "Updates a Channel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ChannelData updateChannel(@PathVariable Long id, @RequestBody ChannelForm channelForm) {
        logger.info("update channel for id : " + id);
        return channelDto.updateChannel(id, channelForm);
    }

    @ApiOperation(value = "Gets all Channels")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChannelData> getAllChannels() {
        logger.info("get all channels");
        return channelDto.getAllChannels();
    }

    @ApiOperation(value = "Gets all Clients")
    @RequestMapping(value = "/allClients", method = RequestMethod.GET)
    public List<ClientData> getAllClients() {
        logger.info("get all clients");
        return channelDto.getAllClients();
    }

    @ApiOperation(value = "Gets all Customers")
    @RequestMapping(value = "/allCustomers", method = RequestMethod.GET)
    public List<ClientData> getAllCustomers() {
        logger.info("get all customers");
        return channelDto.getAllCustomers();
    }

}