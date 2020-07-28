package com.channel.dao;

import com.channel.pojo.ChannelListing;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelListingDao extends AbstractDao {

    // select by parameters
    private static String selectByPrameters= "select c from ChannelListing c where c.channelId=:channelId and c.channelSkuId=:channelSkuId and c.clientId=:clientId";
    // search
    private static String search= "select c from ChannelListing c where c.channelId=:channelId and c.clientId=:clientId";
    // selectAll
    private static String selectAll= "select c from ChannelListing c";

    public ChannelListing selectByParameters(Long channelId, String channelSkuId, Long clientId) {
        TypedQuery<ChannelListing> query = getQuery(selectByPrameters, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        query.setParameter("clientId", clientId);
        return getSingle(query);
    }

    public List<ChannelListing> searchChannelListing(Long clientId, Long channelId) {
        TypedQuery<ChannelListing> query = getQuery(search, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    public List<ChannelListing> selectAll() {
        TypedQuery<ChannelListing> query = getQuery(selectAll, ChannelListing.class);
        return query.getResultList();
    }
}
