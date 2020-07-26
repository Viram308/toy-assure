package com.assure.dao;

import com.assure.pojo.BinSku;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao{

    // select according to binId and globalSkuId
    private static String selectByBinIdGlobalSkuId = "select b from BinSku b where b.binId=:binId and b.globalSkuId=:globalSkuId";
    // select all
    private static String selectAll = "select b from BinSku b";
    // search by globalSkuIdList
    private static String searchByGlobalSkuIdList = "select b from BinSku b where b.globalSkuId IN :globalSkuIdList";

    public List<BinSku> selectAll() {
        TypedQuery<BinSku> query = getQuery(selectAll, BinSku.class);
        return query.getResultList();
    }

    public BinSku selectByBinIdGlobalSkuId(Long binId, Long globalSkuId) {
        TypedQuery<BinSku> query = getQuery(selectByBinIdGlobalSkuId, BinSku.class);
        query.setParameter("binId", binId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    public List<BinSku> searchByGlobalSkuIdList(List<Long> globalSkuIdList) {
        TypedQuery<BinSku> query = getQuery(searchByGlobalSkuIdList, BinSku.class);
        query.setParameter("globalSkuIdList", globalSkuIdList);
        return query.getResultList();
    }
}
