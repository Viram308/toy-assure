package com.channel.util;

import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.commons.util.StringUtil;

public class NormalizeUtil {

    // normalize entity

    public static void normalizeChannel(Channel channel){
        channel.setName(StringUtil.toLowerCase(channel.getName()));
    }

    public static void normalizeChannelListing(ChannelListing channelListing) {
        channelListing.setChannelSkuId(StringUtil.toLowerCase(channelListing.getChannelSkuId()));
    }
}
