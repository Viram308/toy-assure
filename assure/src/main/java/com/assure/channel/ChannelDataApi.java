package com.assure.channel;

import com.commons.response.ChannelData;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ChannelDataApi extends AbstractRestTemplate {
    @Value("${channel.url}")
    private String CHANNEL_URL;

    public List<ChannelData> getAllChannel() {
        String GET_CHANNEL_URL = CHANNEL_URL + "/channel";
        HttpEntity<ChannelData> entity = new HttpEntity<>(getHeaders());
        return Arrays.asList(restTemplate.exchange(GET_CHANNEL_URL, HttpMethod.GET, entity, ChannelData[].class).getBody());
    }

    public ChannelData getChannelDetails(Long channelId) {
        String GET_CHANNEL_URL = CHANNEL_URL + "/channel/" + channelId;
        HttpEntity<ChannelData> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(GET_CHANNEL_URL, HttpMethod.GET, entity, ChannelData.class).getBody();
    }

    public byte[] getPDFBytes(Long id) {
        String GET_CHANNEL_URL = CHANNEL_URL + "/channelOrder/download/" + id;

        HttpEntity<ChannelData> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(GET_CHANNEL_URL, HttpMethod.GET, entity, byte[].class, "1").getBody();

    }

    public void generateInvoice(Long id) {
        String GET_CHANNEL_URL = CHANNEL_URL + "/channelOrder/generateInvoice/" + id;
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        restTemplate.exchange(GET_CHANNEL_URL, HttpMethod.GET, entity, String.class);

    }
}
