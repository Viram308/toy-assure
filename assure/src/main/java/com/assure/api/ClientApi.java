package com.assure.api;

import com.assure.dao.ClientDao;
import com.assure.pojo.Client;
import com.assure.util.NormalizeUtil;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientApi {

    private static final Logger logger = Logger.getLogger(ClientApi.class);

    @Autowired
    private ClientDao clientDao;

    @Transactional(rollbackFor = ApiException.class)
    public Client add(Client client) {
        NormalizeUtil.normalizeClient(client);
        getCheckExisting(client.getName(),client.getType());
        return clientDao.insert(client);
    }

    @Transactional(readOnly = true)
    public Client get(Long id) {
        return clientDao.select(Client.class,id);
    }

    @Transactional(readOnly = true)
    public List<Client> searchByName(String name) {
        name= StringUtil.toLowerCase(name);
        return clientDao.searchByName(name);
    }

    @Transactional
    public Client update(Long id,Client client) {
        NormalizeUtil.normalizeClient(client);
        getCheckExisting(client.getName(),client.getType());
        Client clientUpdate = getCheck(id);
        clientUpdate.setType(client.getType());
        clientUpdate.setName(client.getName());
        return clientDao.update(clientUpdate);
    }

    @Transactional(readOnly = true)
    public List<Client> getAll() {
        return clientDao.selectAll();
    }

    @Transactional(readOnly = true)
    public Client getCheck(Long id){
        Client client = clientDao.select(Client.class,id);
        if (client == null) {
            logger.info("Given Name and Type pair doesn't exists");
            throw new ApiException("Given Name and Type pair doesn't exists");
        }
        return client;
    }

    @Transactional(readOnly = true)
    public void getCheckExisting(String name, ClientType type){
        Client client = clientDao.selectByNameAndType(name, type);
        if (client != null) {
            logger.info("Given Name and Type pair already exists");
            throw new ApiException("Given Name and Type pair already exists");
        }
    }
}
