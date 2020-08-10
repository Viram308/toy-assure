package com.assure.dto;

import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.form.ClientForm;
import com.commons.response.ClientData;
import com.commons.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ClientDtoTest extends AbstractUnitTest {
    private ClientForm clientForm1,clientForm2,clientForm3;
    @Autowired
    private ClientDto clientDto;

    @Before
    public void setUp(){
        clientForm1 = createClientForm("viram", ClientType.CLIENT);
        clientForm2 = createClientForm("viram308",ClientType.CUSTOMER);
        clientForm3 = createClientForm("vir",ClientType.CLIENT);

    }

    private ClientForm createClientForm(String name, ClientType type) {
        ClientForm clientForm = new ClientForm();
        clientForm.setName(name);
        clientForm.setType(type);
        return clientForm;
    }

    // test for adding client
    @Test
    public void testAddClient(){
        ClientData clientData = clientDto.addClient(clientForm1);
        assertNotNull(clientData);
        assertEquals(StringUtil.toLowerCase(clientForm1.getName()),clientData.getName());
        assertEquals(clientForm1.getType().toString(),clientData.getType());
    }

    // test for getting client by id
    @Test
    public void testGetClient(){
        ClientData clientData1 = clientDto.addClient(clientForm1);
        ClientData clientData = clientDto.getClient(clientData1.getId());
        assertNotNull(clientData);
        // test data
        assertEquals(StringUtil.toLowerCase(clientForm1.getName()),clientData.getName());
        assertEquals(clientForm1.getType().toString(),clientData.getType());
        clientData = clientDto.getClient(clientData1.getId()+1);
        assertNull(clientData);
    }

    // test for search clients
    @Test
    public void testSearchClients(){
        clientDto.addClient(clientForm1);
        clientDto.addClient(clientForm2);
        List<ClientData> clientDataList = clientDto.searchClients(clientForm3);
        // list size 1
        assertEquals(1,clientDataList.size());
        assertEquals(StringUtil.toLowerCase(clientForm1.getName()),clientDataList.get(0).getName());
    }

    // test for search by client's name
    @Test
    public void testSearchClientsByName(){
        clientDto.addClient(clientForm1);
        clientDto.addClient(clientForm2);
        List<ClientData> clientDataList = clientDto.searchClientsByName(clientForm3.getName());
        assertEquals(2,clientDataList.size());
    }

    // test for updating client
    @Test
    public void testUpdateClient(){
        ClientData clientData = clientDto.addClient(clientForm1);
        ClientData clientData1= clientDto.updateClient(clientData.getId(),clientForm2);
        assertNotNull(clientData1);
        // test data
        assertEquals(StringUtil.toLowerCase(clientForm2.getName()),clientData1.getName());
        assertEquals(clientForm2.getType().toString(),clientData1.getType());
    }

    // test for getting all data
    @Test
    public void testGetAll(){
        clientDto.addClient(clientForm1);
        clientDto.addClient(clientForm2);
        List<ClientData> clientDataList = clientDto.getAllClients();
        assertEquals(2,clientDataList.size());
    }

    // test for getting only clients
    @Test
    public void testGetOnlyClients(){
        clientDto.addClient(clientForm1);
        clientDto.addClient(clientForm2);
        List<ClientData> clientDataList = clientDto.getOnlyClients();
        assertEquals(1,clientDataList.size());
        assertEquals(StringUtil.toLowerCase(clientForm1.getName()),clientDataList.get(0).getName());
    }

    // test for getting only customers
    @Test
    public void testGetOnlyCustomers(){
        clientDto.addClient(clientForm1);
        clientDto.addClient(clientForm2);
        List<ClientData> clientDataList = clientDto.getOnlyCustomers();
        assertEquals(1,clientDataList.size());
        assertEquals(StringUtil.toLowerCase(clientForm2.getName()),clientDataList.get(0).getName());
    }

    // test for validate
    @Test(expected = ApiException.class)
    public void testValidate(){
        clientDto.validate(clientForm1);
        clientForm1.setName("");
        // throw exception
        clientDto.validate(clientForm1);
    }



}
