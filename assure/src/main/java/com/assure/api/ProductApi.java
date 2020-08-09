package com.assure.api;

import com.assure.dao.ProductDao;
import com.assure.pojo.Product;
import com.assure.util.NormalizeUtil;
import com.commons.api.ApiException;
import com.commons.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductApi {
    @Autowired
    private ProductDao productDao;

    @Transactional
    public Product add(Product product) {
        NormalizeUtil.normalizeProduct(product);
        return productDao.insert(product);
    }

    @Transactional(readOnly = true)
    public Product get(Long globalSkuId) {
        return productDao.select(Product.class, globalSkuId);
    }


    @Transactional(rollbackFor = ApiException.class)
    public Product update(Long globalSkuId, Product product) {
        NormalizeUtil.normalizeProduct(product);
        getCheckExisting(product.getClientSkuId(), product.getClientId());
        Product productUpdate = getCheck(globalSkuId);
        productUpdate.setBrandId(product.getBrandId());
        productUpdate.setName(product.getName());
        productUpdate.setMrp(product.getMrp());
        productUpdate.setDescription(product.getDescription());
        return productDao.update(productUpdate);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productDao.selectAll();
    }

    @Transactional(readOnly = true)
    public Product getCheck(Long globalSkuId) {
        Product product = productDao.select(Product.class, globalSkuId);
        if (product == null) {
            throw new ApiException("Product doesn't exist for globalSkuId : " + globalSkuId);
        }
        return product;
    }

    @Transactional(readOnly = true)
    public void getCheckExisting(String clientSkuId, Long clientId) {
        Product product = getByClientIdAndClientSkuId(clientId, clientSkuId);
        if (product != null) {
            throw new ApiException("Product already exists for clientSkuId :" + clientSkuId + " and clientId :" + clientId);
        }
    }

    @Transactional(readOnly = true)
    public Product getByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
        clientSkuId = StringUtil.toLowerCase(clientSkuId);
        return productDao.selectByClientIdAndClientSkuId(clientId, clientSkuId);
    }


    public List<Product> getByClientId(Long clientId) {
        return productDao.selectByClientId(clientId);
    }
}
