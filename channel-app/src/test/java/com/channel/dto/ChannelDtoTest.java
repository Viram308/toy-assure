package com.channel.dto;

import com.channel.model.form.ChannelForm;
import com.channel.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.enums.InvoiceType;
import com.commons.response.ChannelData;
import com.commons.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ChannelDtoTest extends AbstractUnitTest {
    private ChannelForm channelForm1,channelForm2;

    @Autowired
    private ChannelDto channelDto;
    @Autowired
    private ChannelListingDto channelListingDto;

    @Before
    public void setUp(){
        channelForm1 = createChannelForm("chanNel1","channel");
        channelForm2 = createChannelForm("chaN","");
    }

    private ChannelForm createChannelForm(String name,String type) {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setChannelName(name);
        channelForm.setInvoiceType(type);
        return channelForm;
    }

    @Test
    public void testAddChannel(){
        ChannelData channelData = channelDto.addChannel(channelForm1);
        assertNotNull(channelData);
        assertEquals(StringUtil.toLowerCase(channelForm1.getChannelName()),channelData.getName());
        assertEquals(InvoiceType.CHANNEL.toString(),channelData.getInvoiceType());
    }

    @Test
    public void testGetChannel(){
        ChannelData channelData1 = channelDto.addChannel(channelForm1);
        ChannelData channelData2 = channelDto.getChannel(channelData1.getId());
        assertNotNull(channelData2);
        assertEquals(StringUtil.toLowerCase(channelForm1.getChannelName()),channelData2.getName());
        assertEquals(InvoiceType.CHANNEL.toString(),channelData2.getInvoiceType());
    }

    @Test
    public void testSearchChannel(){
        channelDto.addChannel(channelForm1);
        List<ChannelData> channelDataList = channelDto.searchChannels(channelForm2);
        assertEquals(1,channelDataList.size());
        channelForm2.setInvoiceType("self");
        channelDataList = channelDto.searchChannels(channelForm2);
        assertEquals(0,channelDataList.size());
    }

    @Test
    public void testUpdateChannel(){
        ChannelData channelData = channelDto.addChannel(channelForm1);
        channelForm2.setInvoiceType("self");
        ChannelData channelData1 = channelDto.updateChannel(channelData.getId(),channelForm2);
        assertNotNull(channelData1);
        assertEquals(StringUtil.toLowerCase(channelForm2.getChannelName()),channelData1.getName());
        assertEquals(channelForm2.getInvoiceType(),StringUtil.toLowerCase(channelData1.getInvoiceType()));
    }

    @Test
    public void testGetAll(){
        channelDto.addChannel(channelForm1);
        List<ChannelData> channelDataList = channelDto.getAllChannels();
        assertTrue(channelDataList.size()>0);
        assertEquals(1,channelDataList.size());
    }

    @Test(expected = ApiException.class)
    public void testValidate(){
        channelDto.validate(channelForm1);
        // throw exception
        channelDto.validate(channelForm2);
    }


}
