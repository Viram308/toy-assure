package com.assure.controller;

import com.assure.dto.ClientDto;
import com.commons.form.ClientForm;
import com.commons.response.ClientData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ClientData addClient(@RequestBody ClientForm clientForm) throws Exception {
        logger.info("adding client");
        return clientDto.addClient(clientForm);
    }

    @ApiOperation(value = "Gets a Client")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ClientData getClient(@PathVariable Long id) {
        logger.info("get client for id : "+id);
        return clientDto.getClient(id);
    }

    @ApiOperation(value = "Search Clients")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<ClientData> search(@RequestBody ClientForm clientForm) {
        logger.info("search clients");
        return clientDto.searchClients(clientForm);
    }

    @ApiOperation(value = "Search Clients By Name")
    @RequestMapping(value = "/searchByName/{name}", method = RequestMethod.GET)
    public List<ClientData> searchByName(@PathVariable String name) {
        logger.info("search clients by name");
        return clientDto.searchClientsByName(name);
    }

    @ApiOperation(value = "Updates a Client")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ClientData updateClient(@PathVariable Long id, @RequestBody ClientForm clientForm) {
        logger.info("update client for id : "+id);
        return clientDto.updateClient(id, clientForm);
    }



    @ApiOperation(value = "Gets only type Client")
    @RequestMapping(value = "/allClients", method = RequestMethod.GET)
    public List<ClientData> getOnlyClients() {
        logger.info("get only clients");
        return clientDto.getOnlyClients();
    }

    @ApiOperation(value = "Gets only type Customer")
    @RequestMapping(value = "/allCustomers", method = RequestMethod.GET)
    public List<ClientData> getOnlyCustomers() {
        logger.info("get only customers");
        return clientDto.getOnlyCustomers();
    }

    @ApiOperation(value = "Gets all Clients")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ClientData> getAllClients() {
        logger.info("get all clients");
        return clientDto.getAllClients();
    }
}