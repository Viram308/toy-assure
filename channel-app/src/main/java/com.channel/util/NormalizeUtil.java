package com.channel.util;

import com.channel.pojo.Channel;
import com.commons.util.StringUtil;

public class NormalizeUtil {
    public static void normalizeChannel(Channel channel){
        channel.setName(StringUtil.toLowerCase(channel.getName()));
    }
}
