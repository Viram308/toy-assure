package com.channel.api;

import com.channel.pojo.ChannelListing;
import com.channel.spring.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ChannelListingApiTest extends AbstractUnitTest {
    private ChannelListing channelListing1, channelListing2, channelListing3;

    @Autowired
    private ChannelListingApi channelListingApi;

    @Before
    public void setUp(){
        channelListing1 = createObject(1L, 2L, 3L, "abc");
        channelListing2 = createObject(1L, 5L, 6L, "pqr");
        channelListing3 = createObject(7L, 8L, 6L, "xyz");
    }

    public ChannelListing createObject(Long clientId, Long globalSkuId, Long channelId, String channelSkuId) {
        ChannelListing channelListing = new ChannelListing();
        channelListing.setClientId(clientId);
        channelListing.setGlobalSkuId(globalSkuId);
        channelListing.setChannelId(channelId);
        channelListing.setChannelSkuId(channelSkuId);
        return channelListing;
    }

    @Test
    public void testAddListing() {
        channelListingApi.add(channelListing1);
        ChannelListing newChannelListing = channelListingApi.getChannelListing(channelListing1.getId());
        assertNotNull(newChannelListing);
        assertTrue( newChannelListing.getId()>0);
        assertEquals(channelListing1.getClientId(), newChannelListing.getClientId());
        assertEquals(channelListing1.getGlobalSkuId(), newChannelListing.getGlobalSkuId());
        assertEquals(channelListing1.getChannelId(), newChannelListing.getChannelId());
        assertEquals(channelListing1.getChannelSkuId(), newChannelListing.getChannelSkuId());
    }

    @Test
    public void testGetChannelListingByParameters() {
        channelListingApi.add(channelListing1);
        ChannelListing newChannelListing = channelListingApi.getChannelListingByParameters(channelListing1.getChannelId(),channelListing1.getChannelSkuId(),channelListing1.getClientId());
        assertNotNull(newChannelListing);
        assertTrue( newChannelListing.getId()>0);
        assertEquals(channelListing1.getClientId(), newChannelListing.getClientId());
        assertEquals(channelListing1.getGlobalSkuId(), newChannelListing.getGlobalSkuId());
        assertEquals(channelListing1.getChannelId(), newChannelListing.getChannelId());
        assertEquals(channelListing1.getChannelSkuId(), newChannelListing.getChannelSkuId());
    }

    @Test
    public void testGetChannelListingByClientId() {
        channelListingApi.add(channelListing1);
        channelListingApi.add(channelListing2);
        List<ChannelListing> channelListingList = channelListingApi.getByClientId(channelListing1.getClientId());
        assertEquals(2,channelListingList.size());
    }

    @Test
    public void testGetChannelListingByParametersLong() {
        channelListingApi.add(channelListing1);
        ChannelListing newChannelListing = channelListingApi.getChannelListingByParameters(channelListing1.getChannelId(),channelListing1.getClientId(),channelListing1.getGlobalSkuId());
        assertNotNull(newChannelListing);
        assertTrue( newChannelListing.getId()>0);
        assertEquals(channelListing1.getClientId(), newChannelListing.getClientId());
        assertEquals(channelListing1.getGlobalSkuId(), newChannelListing.getGlobalSkuId());
        assertEquals(channelListing1.getChannelId(), newChannelListing.getChannelId());
        assertEquals(channelListing1.getChannelSkuId(), newChannelListing.getChannelSkuId());
    }

    @Test
    public void testGetAll() {
        channelListingApi.add(channelListing1);
        channelListingApi.add(channelListing2);
        channelListingApi.add(channelListing3);
        List<ChannelListing> channelListingList = channelListingApi.getAllChannelListing();
        assertEquals(3,channelListingList.size());
    }

    @Test
    public void testGetListing(){
        channelListingApi.add(channelListing1);
        ChannelListing newChannelListing = channelListingApi.getChannelListing(channelListing1.getId());
        assertNotNull(newChannelListing);
        assertTrue( newChannelListing.getId()>0);
        assertEquals(channelListing1.getClientId(), newChannelListing.getClientId());
        assertEquals(channelListing1.getGlobalSkuId(), newChannelListing.getGlobalSkuId());
        assertEquals(channelListing1.getChannelId(), newChannelListing.getChannelId());
        assertEquals(channelListing1.getChannelSkuId(), newChannelListing.getChannelSkuId());
    }

}
