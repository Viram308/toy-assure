package com.channel.dao;

import com.channel.pojo.Channel;
import com.commons.enums.InvoiceType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao {

    // select according to brand and category
    private static String selectByNameType = "select c from Channel c where c.name=:name and c.invoiceType=:type";
    // select all
    private static String selectAll = "select c from Channel c";
    // search
    private static String searchByName = "select c from Channel c where c.name like :name";

    // select according to brand and category
    public Channel selectByNameAndType(String name, InvoiceType type) {
        TypedQuery<Channel> query = getQuery(selectByNameType, Channel.class);
        query.setParameter("name", name);
        query.setParameter("type", type);
        return getSingle(query);
    }

    // select all
    public List<Channel> selectAll() {
        TypedQuery<Channel> query = getQuery(selectAll, Channel.class);
        return query.getResultList();
    }

    public List<Channel> searchByName(String name) {
        TypedQuery<Channel> query = getQuery(searchByName, Channel.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }
}
