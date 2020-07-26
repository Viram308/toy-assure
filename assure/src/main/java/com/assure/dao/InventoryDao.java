package com.assure.dao;

import com.assure.pojo.Inventory;
import com.assure.pojo.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{

    // select according to globalSkuId
    private static String selectByGlobalSkuId = "select i from Inventory i where i.globalSkuId=:globalSkuId";
    // select all
    private static String selectAll = "select i from Inventory i";
    // search according to globalSkuIdList
    private static String searchByGlobalSkuIdList = "select i from Inventory i where i.globalSkuId IN :globalSkuIdList";


    public List<Inventory> selectAll() {
        TypedQuery<Inventory> query = getQuery(selectAll, Inventory.class);
        return query.getResultList();
    }

    public Inventory selectByGlobalSkuId(Long globalSkuId) {
        TypedQuery<Inventory> query = getQuery(selectByGlobalSkuId, Inventory.class);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    public List<Inventory> searchByGlobalSkuIdList(List<Long> globalSkuIdList) {
        TypedQuery<Inventory> query = getQuery(searchByGlobalSkuIdList, Inventory.class);
        query.setParameter("globalSkuIdList", globalSkuIdList);
        return query.getResultList();
    }
}
