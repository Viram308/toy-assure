package com.assure.controller;

import java.util.List;

import com.assure.dto.ClientDto;
import com.assure.model.form.ClientForm;
import com.assure.pojo.Client;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/client")
public class ClientApiController {

    private static final Logger logger = Logger.getLogger(ClientApiController.class);

    @Autowired
    private ClientDto clientDto;
    // CRUD operations for client

    @ApiOperation(value = "Adds a Client")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Client addClient(@RequestBody ClientForm clientForm) throws Exception {
        logger.info("adding client");
        return clientDto.addClient(clientForm);
    }

    @ApiOperation(value = "Gets a Client")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Client getClient(@PathVariable Long id) {
        logger.info("get client for id : "+id);
        return clientDto.getClient(id);
    }

    @ApiOperation(value = "Search Clients")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<Client> search(@RequestBody ClientForm clientForm) {
        logger.info("search clients");
        return clientDto.searchClients(clientForm);
    }

    @ApiOperation(value = "Search Clients By Name")
    @RequestMapping(value = "/searchByName/{name}", method = RequestMethod.GET)
    public List<Client> searchByName(@PathVariable String name) {
        logger.info("search clients by name");
        return clientDto.searchClientsByName(name);
    }

    @ApiOperation(value = "Updates a Client")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Client updateClient(@PathVariable Long id, @RequestBody ClientForm clientForm) {
        logger.info("update client for id : "+id);
        return clientDto.updateClient(id, clientForm);
    }



    @ApiOperation(value = "Gets only type Client")
    @RequestMapping(value = "/allClients", method = RequestMethod.GET)
    public List<Client> getOnlyClients() {
        logger.info("get only clients");
        return clientDto.getOnlyClients();
    }

    @ApiOperation(value = "Gets only type Customer")
    @RequestMapping(value = "/allCustomers", method = RequestMethod.GET)
    public List<Client> getOnlyCustomers() {
        logger.info("get only customers");
        return clientDto.getOnlyCustomers();
    }

    @ApiOperation(value = "Gets all Clients")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Client> getAllClients() {
        logger.info("get all clients");
        return clientDto.getAllClients();
    }
}