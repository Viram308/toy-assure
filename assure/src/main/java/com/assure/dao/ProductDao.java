package com.assure.dao;

import com.assure.pojo.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {


    // select according to clientId and clientSkuId
    private static String selectByClientSkuIdClientId = "select p from Product p where p.clientSkuId=:clientSkuId and p.clientId=:clientId";
    // select all
    private static String selectAll = "select p from Product p";
    // search
    private static String searchByName = "select p from Product p where p.name like :name";
    // select according to clientId
    private static String selectByClientId = "select p from Product p where p.clientId=:clientId";

    // select all
    public List<Product> selectAll() {
        TypedQuery<Product> query = getQuery(selectAll, Product.class);
        return query.getResultList();
    }

    public List<Product> searchByName(String name) {
        TypedQuery<Product> query = getQuery(searchByName, Product.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }

    public Product selectByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
        TypedQuery<Product> query = getQuery(selectByClientSkuIdClientId, Product.class);
        query.setParameter("clientSkuId", clientSkuId);
        query.setParameter("clientId", clientId);
        return getSingle(query);
    }

    public List<Product> selectByClientId(Long clientId) {
        TypedQuery<Product> query = getQuery(selectByClientId, Product.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
}
