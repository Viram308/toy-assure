package com.assure.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.assure.pojo.Client;
import com.commons.enums.ClientType;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao extends AbstractDao {

    // select according to brand and category
    private static String selectByNameType = "select c from Client c where c.name=:name and c.type=:type";
    // select all
    private static String selectAll = "select c from Client c";
    // search
    private static String searchByName = "select c from Client c where c.name like :name";

    @PersistenceContext
    private EntityManager em;

    // select according to brand and category
    public Client selectByNameAndType(String name, ClientType type) {
        TypedQuery<Client> query = getQuery(selectByNameType, Client.class);
        query.setParameter("name", name);
        query.setParameter("type", type);
        return getSingle(query);
    }

    // select all
    public List<Client> selectAll() {
        TypedQuery<Client> query = getQuery(selectAll, Client.class);
        return query.getResultList();
    }

    public List<Client> searchByName(String name) {
        TypedQuery<Client> query = getQuery(searchByName, Client.class);
        query.setParameter("name", name+"%");
        return query.getResultList();
    }

}
