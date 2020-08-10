package com.assure.api;

import com.assure.dao.BinDao;
import com.assure.pojo.Bin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinApi {

    @Autowired
    private BinDao binDao;

    @Transactional
    public Long add(Bin bin) {
       Bin bin1 = binDao.insert(bin);
       // return added binId
       return bin1.getBinId();
    }

    @Transactional(readOnly = true)
    public List<Bin> getAll() {
        return binDao.selectAll();
    }

    @Transactional(readOnly = true)
    public Bin getBin(Long binId) {
        return binDao.select(Bin.class,binId);
    }

}
