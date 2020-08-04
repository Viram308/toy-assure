package com.assure.dto;

import com.assure.api.ClientApi;
import com.commons.form.ClientForm;
import com.assure.pojo.Client;
import com.assure.util.ConverterUtil;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.response.ClientData;
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
    public ClientData addClient(ClientForm clientForm) {
        validate(clientForm);
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        client=clientApi.add(client);
        logger.info("Client added");
        return ConverterUtil.convertClientToClientData(client);
    }

    @Transactional(readOnly = true)
    public ClientData getClient(Long id) {
        Client client = clientApi.get(id);
        if(client != null){
            return ConverterUtil.convertClientToClientData(client);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ClientData> searchClients(ClientForm clientForm) {
        List<Client> clientList = clientApi.searchByName(clientForm.getName());
        clientList = clientList.stream().filter(o-> (clientForm.getType().equals(o.getType()))).collect(Collectors.toList());
        logger.info("Number of clients returned : "+clientList.size());
        return clientList.stream().map(ConverterUtil::convertClientToClientData).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientData> searchClientsByName(String name) {
        List<Client> clientList = clientApi.searchByName(name);
        logger.info("Number of clients returned while search by name : "+clientList.size());
        return clientList.stream().map(ConverterUtil::convertClientToClientData).collect(Collectors.toList());
    }

    @Transactional
    public ClientData updateClient(Long id, ClientForm clientForm) {
        validate(clientForm);
        Client client = ConverterUtil.convertClientFormToClient(clientForm);
        client=clientApi.update(id,client);
        logger.info("Client updated");
        return ConverterUtil.convertClientToClientData(client);
    }

    @Transactional(readOnly = true)
    public List<ClientData> getAllClients() {
        List<Client> clientList = clientApi.getAll();
        logger.info("Number of clients returned : "+clientList.size());
        return clientList.stream().map(ConverterUtil::convertClientToClientData).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientData> getOnlyClients() {
        List<Client> clientList = clientApi.getAllClients();
        return clientList.stream().map(ConverterUtil::convertClientToClientData).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ClientData> getOnlyCustomers() {
        List<Client> clientList = clientApi.getAllCustomers();
        return clientList.stream().map(ConverterUtil::convertClientToClientData).collect(Collectors.toList());
    }

    public void validate(ClientForm clientForm) {
        String type=StringUtil.toUpperCase(clientForm.getType().toString());
        if(StringUtil.isEmpty(clientForm.getName()) || (!type.equals(ClientType.CLIENT.toString()) && !type.equals(ClientType.CUSTOMER.toString()))){
            throw new ApiException("Enter valid name and type");
        }
    }


}
