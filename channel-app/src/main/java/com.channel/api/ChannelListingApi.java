package com.channel.api;

import com.channel.dao.ChannelListingDao;
import com.channel.model.form.ChannelListingSearchForm;
import com.channel.pojo.ChannelListing;
import com.commons.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelListingApi {

    @Autowired
    private ChannelListingDao channelListingDao;

    @Transactional
    public void add(ChannelListing channelListing) {
        channelListingDao.insert(channelListing);
    }

    @Transactional(readOnly = true)
    public ChannelListing getChannelListingByParameters(Long channelId, String channelSkuId, Long clientId) {
        channelSkuId = StringUtil.toLowerCase(channelSkuId);
        return channelListingDao.selectByParameters(channelId,channelSkuId,clientId);
    }

    @Transactional(readOnly = true)
    public List<ChannelListing> getAllChannelListing() {
        return channelListingDao.selectAll();
    }
}
