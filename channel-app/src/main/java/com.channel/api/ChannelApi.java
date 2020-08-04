package com.channel.api;

import com.channel.dao.ChannelDao;
import com.channel.pojo.Channel;
import com.channel.util.NormalizeUtil;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.enums.InvoiceType;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelApi {

    private static final Logger logger = Logger.getLogger(ChannelApi.class);

    @Autowired
    private ChannelDao channelDao;

    @Transactional(rollbackFor = ApiException.class)
    public Channel add(Channel channel) {
        NormalizeUtil.normalizeChannel(channel);
        getCheckExisting(channel.getName(),channel.getInvoiceType());
        return channelDao.insert(channel);
    }

    @Transactional(readOnly = true)
    public Channel get(Long id) {
        return channelDao.select(Channel.class, id);
    }

    @Transactional(readOnly = true)
    public List<Channel> getByChannelIdList(List<Long> channelIdList) {
        return channelDao.selectByIdList(channelIdList);
    }

    @Transactional(readOnly = true)
    public List<Channel> searchByName(String channelName) {
        channelName= StringUtil.toLowerCase(channelName);
        return channelDao.searchByName(channelName);
    }

    @Transactional(rollbackFor = ApiException.class)
    public Channel update(Long id, Channel channel) {
        NormalizeUtil.normalizeChannel(channel);
        getCheckExisting(channel.getName(),channel.getInvoiceType());
        Channel channelUpdate = getCheck(id);
        channelUpdate.setInvoiceType(channel.getInvoiceType());
        channelUpdate.setName(channel.getName());
        return channelDao.update(channelUpdate);
    }

    @Transactional(readOnly = true)
    public List<Channel> getAll() {
        return channelDao.selectAll();
    }

    @Transactional(readOnly = true)
    public Channel getCheck(Long id){
        Channel channel = channelDao.select(Channel.class,id);
        if (channel == null) {
            logger.info("Given Name and Type pair doesn't exists");
            throw new ApiException("Given Name and Type pair doesn't exists");
        }
        return channel;
    }

    @Transactional(readOnly = true)
    public void getCheckExisting(String name, InvoiceType type){
        name = StringUtil.toLowerCase(name);
        Channel channel = channelDao.selectByNameAndType(name, type);
        if (channel != null) {
            logger.info("Given Name and Type pair already exists");
            throw new ApiException("Given Name and Type pair already exists");
        }
    }


}
