package com.channel.controller;

import com.channel.dto.ChannelDto;
import com.channel.dto.ChannelListingDto;
import com.channel.model.form.ChannelForm;
import com.channel.model.form.ChannelListingCsvForm;
import com.channel.model.form.ChannelListingSearchForm;
import com.channel.model.response.ChannelListingData;
import com.channel.pojo.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/channelListing")
public class ChannelListingController {
    private static final Logger logger = Logger.getLogger(ChannelListingController.class);

    @Autowired
    private ChannelListingDto channelListingDto;
    // CRUD operations for channel

    @ApiOperation(value = "Adds a Channel Listing")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void addChannelListing(@RequestBody ChannelListingCsvForm channelListingCsvForm, BindingResult result) throws Exception {
        logger.info("adding channel listing");
        channelListingDto.addChannelListing(channelListingCsvForm,result);
    }

    @ApiOperation(value = "Search Channels")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<ChannelListingData> search(@RequestBody ChannelListingSearchForm channelListingSearchForm) {
        logger.info("search channel Listing");
        return channelListingDto.searchChannelListing(channelListingSearchForm);
    }

    @ApiOperation(value = "Gets all Channel Listing")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChannelListingData> getAllChannelListings() {
        logger.info("get all channel listing");
        return channelListingDto.getAllChannelListing();
    }
}
