package com.channel.api;

import com.channel.pojo.Channel;
import com.channel.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.enums.InvoiceType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ChannelApiTest extends AbstractUnitTest {
    private Channel channel1, channel2;

    @Autowired
    private ChannelApi channelApi;

    @Before
    public void setUp() {
        channel1 = createObject("test1", InvoiceType.CHANNEL);
        channel2 = createObject("test2", InvoiceType.SELF);
    }

    public Channel createObject(String name, InvoiceType type) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setInvoiceType(type);
        return channel;
    }

    // test for adding channel
    @Test
    public void testAddChannel() {
        Channel newChannel = channelApi.add(channel1);
        assertNotNull(newChannel);
        assertTrue(newChannel.getId() > 0);
        // test data
        assertEquals(channel1.getName(), newChannel.getName());
        assertEquals(channel1.getInvoiceType(), newChannel.getInvoiceType());
    }

    // test for updating channel
    @Test
    public void testUpdateChannel() {
        channelApi.add(channel2);
        Channel newChannel = new Channel();
        // update
        newChannel.setName("updatedName");
        newChannel.setInvoiceType(InvoiceType.CHANNEL);
        channelApi.update(channel2.getId(), newChannel);
        Channel result = channelApi.get(channel2.getId());
        assertNotNull(result);
        // test data
        assertEquals(newChannel.getName(), result.getName());
        assertEquals(newChannel.getInvoiceType(), result.getInvoiceType());
    }

    // test for getting channel by id
    @Test
    public void testGetChannelById() {
        channelApi.add(channel1);
        Channel channel = channelApi.get(channel1.getId());
        assertNotNull(channel);
        // test data
        assertEquals(channel1.getName(), channel.getName());
        assertEquals(channel1.getInvoiceType(), channel.getInvoiceType());
    }

    // test for getting channels by channelIdList
    @Test
    public void testGetByChannelIdList() {
        channelApi.add(channel1);
        List<Long> channelIdList = new ArrayList<>();
        channelIdList.add(channel1.getId());
        List<Channel> channelList = channelApi.getByChannelIdList(channelIdList);
        assertEquals(1,channelList.size());
        channelApi.add(channel2);
        channelIdList.add(channel2.getId());
        channelList = channelApi.getByChannelIdList(channelIdList);
        assertEquals(2,channelList.size());
    }

    // test for search by name
    @Test
    public void testSearchByName() {
        channelApi.add(channel1);
        channelApi.add(channel2);
        List<Channel> channelList = channelApi.searchByName("test");
        assertEquals(2, channelList.size());
        // update name
        channelList = channelApi.searchByName("test1");
        assertEquals(1, channelList.size());
    }

    // test for getting all channel
    @Test
    public void testGetAllChannelDetails() {
        channelApi.add(channel1);
        channelApi.add(channel2);
        List<Channel> list = channelApi.getAll();

        assertTrue(list.size() > 0);
        // test data
        assertEquals(channel1.getName(), list.get(0).getName());
        assertEquals(channel1.getInvoiceType(), list.get(0).getInvoiceType());
        assertEquals(channel2.getName(), list.get(1).getName());
        assertEquals(channel2.getInvoiceType(), list.get(1).getInvoiceType());
    }

    // test for getCheck
    @Test(expected = ApiException.class)
    public void testGetCheck(){
        channelApi.add(channel1);
        Channel channel = channelApi.getCheck(channel1.getId());
        assertNotNull(channel);
        // throw exception
        channelApi.getCheck(channel1.getId()+1);
    }

    // test for existing data
    @Test(expected = ApiException.class)
    public void testGetCheckExisting(){
        channelApi.add(channel1);
        channelApi.getCheckExisting("a",InvoiceType.CHANNEL);
        // throw exception
        channelApi.getCheckExisting(channel1.getName(),channel1.getInvoiceType());
    }
}
