package com.assure.dto;

import com.assure.api.ClientApi;
import com.assure.model.form.ClientForm;
import com.assure.pojo.Client;
import com.assure.util.ConverterUtil;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientDto {

    private static final Logger logger = Logger.getLogger(ClientDto.class);

    @Autowired
    private ClientApi clientApi;

    @Transactional
    public Client addClient(ClientForm clientForm) {
        validate(clientForm);
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        client=clientApi.add(client);
        logger.info("Client added");
        return client;
    }

    @Transactional(readOnly = true)
    public Client getClient(Long id) {
        return clientApi.get(id);
    }

    @Transactional(readOnly = true)
    public List<Client> searchClients(ClientForm clientForm) {
        List<Client> clientList = clientApi.searchByName(clientForm.getName());
        clientList = clientList.stream().filter(o-> (clientForm.getType().equals(o.getType()))).collect(Collectors.toList());
        logger.info("Number of clients returned : "+clientList.size());
        return clientList;
    }

    @Transactional(readOnly = true)
    public List<Client> searchClientsByName(String name) {
        List<Client> clientList = clientApi.searchByName(name);
        logger.info("Number of clients returned while search by name : "+clientList.size());
        return clientList;
    }

    @Transactional
    public Client updateClient(Long id, ClientForm clientForm) {
        validate(clientForm);
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        client=clientApi.update(id,client);
        logger.info("Client updated");
        return client;
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        List<Client> clientList = clientApi.getAll();
        logger.info("Number of clients returned : "+clientList.size());
        return clientList;
    }

    public void validate(ClientForm clientForm) {
        String type=StringUtil.toUpperCase(clientForm.getType().toString());
        if(StringUtil.isEmpty(clientForm.getName()) || (!type.equals(ClientType.CLIENT.toString()) && !type.equals(ClientType.CUSTOMER.toString()))){
            throw new ApiException("Enter valid name and type");
        }
    }
}
