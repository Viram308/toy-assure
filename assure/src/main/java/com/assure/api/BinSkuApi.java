package com.assure.api;

import com.assure.dao.BinSkuDao;
import com.assure.model.response.BinSkuData;
import com.assure.pojo.Bin;
import com.assure.pojo.BinSku;
import com.commons.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinSkuApi {

    @Autowired
    private BinSkuDao binSkuDao;

    @Transactional
    public BinSku add(BinSku binSku) {
        return binSkuDao.insert(binSku);
    }

    @Transactional(readOnly = true)
    public BinSku get(Long binSkuId) {
        return binSkuDao.select(BinSku.class,binSkuId);
    }

    @Transactional(readOnly = true)
    public List<BinSku> getAll() {
        return binSkuDao.selectAll();
    }

    @Transactional(readOnly = true)
    public BinSku getBinSkuByBinIdGlobalSkuId(Long binId, Long globalSkuId) {
        return binSkuDao.selectByBinIdGlobalSkuId(binId,globalSkuId);
    }

    @Transactional
    public BinSku update(Long id, BinSku binSkuUpdate) {
        BinSku binSku = getCheck(id);
        binSku.setQuantity(binSkuUpdate.getQuantity());
        return binSkuDao.update(binSku);
    }

    @Transactional(readOnly = true)
    public BinSku getCheck(Long id) {
        BinSku binSku = binSkuDao.select(BinSku.class,id);
        if(binSku == null){
            throw new ApiException("BinSku doesn't exist for binSkuId : "+id);
        }
        return binSku;
    }

    @Transactional(readOnly = true)
    public List<BinSku> searchByGlobalSkuIdList(List<Long> globalSkuIdList) {
        return binSkuDao.searchByGlobalSkuIdList(globalSkuIdList);
    }
}
