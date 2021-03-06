package com.channel.dao;

import com.channel.pojo.ChannelListing;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelListingDao extends AbstractDao {

    // select by parameters
    private static String selectByParameters= "select c from ChannelListing c where c.channelId=:channelId and c.channelSkuId=:channelSkuId and c.clientId=:clientId";
    private static String selectByParametersLong= "select c from ChannelListing c where c.channelId=:channelId and c.globalSkuId=:globalSkuId and c.clientId=:clientId";
    private static String selectByClientId= "select c from ChannelListing c where c.clientId=:clientId";

    // selectAll
    private static String selectAll= "select c from ChannelListing c";

    public ChannelListing selectByParameters(Long channelId, String channelSkuId, Long clientId) {
        TypedQuery<ChannelListing> query = getQuery(selectByParameters, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        query.setParameter("clientId", clientId);
        return getSingle(query);
    }

    public ChannelListing selectByParameters(Long channelId, Long clientId,Long globalSkuId) {
        TypedQuery<ChannelListing> query = getQuery(selectByParametersLong, ChannelListing.class);
        query.setParameter("channelId", channelId);
        query.setParameter("clientId", clientId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    public List<ChannelListing> selectAll() {
        TypedQuery<ChannelListing> query = getQuery(selectAll, ChannelListing.class);
        return query.getResultList();
    }

    public List<ChannelListing> selectByClientId(Long clientId) {
        TypedQuery<ChannelListing> query = getQuery(selectByClientId, ChannelListing.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
}
