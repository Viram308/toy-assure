package com.assure.dao;

import com.assure.pojo.Bin;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao extends AbstractDao{

    // select all
    private static String selectAll = "select b from Bin b";

    public List<Bin> selectAll() {
        TypedQuery<Bin> query = getQuery(selectAll, Bin.class);
        return query.getResultList();
    }
}
