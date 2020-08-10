package com.channel.util;

import com.channel.pojo.Channel;
import com.channel.spring.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalizeUtilTest extends AbstractUnitTest {
    private Channel channel;
    @Before
    public void setUp(){
        channel = createObject();
    }

    private Channel createObject() {
        Channel channel = new Channel();
        channel.setName("      ChaNNel   ");
        channel.setInvoiceType(InvoiceType.CHANNEL);
        channel.setId(1L);
        return channel;
    }

    // test for normalize channel
    @Test
    public void testNormalizeChannel(){
        NormalizeUtil.normalizeChannel(channel);
        assertEquals("channel",channel.getName());
    }
}
