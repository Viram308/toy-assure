package com.channel.dto;

import com.channel.api.ChannelApi;
import com.channel.assure.ClientAssure;
import com.channel.model.form.ChannelForm;
import com.channel.model.response.ChannelData;
import com.channel.pojo.Channel;
import com.channel.util.ConverterUtil;
import com.commons.api.ApiException;
import com.commons.enums.InvoiceType;
import com.commons.response.ClientData;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelDto {

    private static final Logger logger = Logger.getLogger(ChannelDto.class);

    @Autowired
    private ChannelApi channelApi;
    @Autowired
    private ClientAssure clientAssure;

    @Transactional(rollbackFor = ApiException.class)
    public ChannelData addChannel(ChannelForm channelForm) {
        validate(channelForm);
        Channel channel = ConverterUtil.convertChannelFormToChannel(channelForm);
        channel = channelApi.add(channel);
        logger.info("channel added");
        return ConverterUtil.convertChannelToChannelData(channel);
    }

    @Transactional(readOnly = true)
    public ChannelData getChannel(Long id) {
        Channel channel = channelApi.get(id);
        return ConverterUtil.convertChannelToChannelData(channel);
    }

    @Transactional(readOnly = true)
    public List<ChannelData> searchChannels(ChannelForm channelForm) {
        List<Channel> channelList=channelApi.searchByName(channelForm.getChannelName());
        if(StringUtil.isEmpty(channelForm.getInvoiceType())){
            return channelList.stream().map(ConverterUtil::convertChannelToChannelData).collect(Collectors.toList());
        }
        String type = StringUtil.toUpperCase(channelForm.getInvoiceType());
        channelList = channelList.stream().filter(o->(o.getInvoiceType().toString().equals(type))).collect(Collectors.toList());
        return channelList.stream().map(ConverterUtil::convertChannelToChannelData).collect(Collectors.toList());
    }

    @Transactional
    public ChannelData updateChannel(Long id, ChannelForm channelForm) {
        validate(channelForm);
        Channel channel = ConverterUtil.convertChannelFormToChannel(channelForm);
        channel = channelApi.update(id,channel);
        logger.info("channel updated");
        return ConverterUtil.convertChannelToChannelData(channel);
    }

    @Transactional(readOnly = true)
    public List<ChannelData> getAllChannels() {
        List<Channel> channelList = channelApi.getAll();
        return channelList.stream().map(ConverterUtil::convertChannelToChannelData).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ClientData> getAllClients() {
        return clientAssure.getClientDetails();
    }

    @Transactional(readOnly = true)
    public List<ClientData> getAllCustomers() {
        return clientAssure.getCustomerDetails();
    }

    private void validate(ChannelForm channelForm) {
        String type=StringUtil.toUpperCase(channelForm.getInvoiceType());
        if(StringUtil.isEmpty(channelForm.getChannelName()) || !(type.equals(InvoiceType.CHANNEL.toString()) || type.equals(InvoiceType.SELF.toString()))){
            throw new ApiException("Please enter channel name and type !!");
        }
    }
}
