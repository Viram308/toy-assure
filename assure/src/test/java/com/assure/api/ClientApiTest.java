package com.assure.api;

import com.assure.pojo.Client;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.enums.ClientType;
import com.commons.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientApiTest extends AbstractUnitTest {

    private Client client1, client2, newClient1;
    @Autowired 
    private ClientApi clientApi;

    @Before
    public void setUp(){
        client1 = createObject("testClient", ClientType.CLIENT);
        client2 = createObject("testCustomer", ClientType.CUSTOMER);
    }

    public Client createObject(String name, ClientType type) {
        Client client = new Client();
        client.setName(name);
        client.setType(type);
        return client;
    }

    @Test
    public void testAddClient() {
        newClient1 = clientApi.add(client1);
        assertNotNull(newClient1);
        assertEquals(StringUtil.toLowerCase(client1.getName()), newClient1.getName());
        assertEquals(client1.getType(), newClient1.getType());

        Client newClient2 = clientApi.add(client2);
        assertNotNull(newClient2);
        assertEquals(StringUtil.toLowerCase(client2.getName()), newClient2.getName());
        assertEquals(client2.getType(), newClient2.getType());
    }

    @Test
    public void testGetClientDetails() {
        clientApi.add(client1);
        Client client = clientApi.get(client1.getId());
        assertNotNull(client);
        assertEquals(client1.getId(), client.getId());
        assertEquals(StringUtil.toLowerCase(client1.getName()), client.getName());
        assertEquals(client1.getType(), client.getType());
    }
    
    @Test
    public void testSearchByName(){
        clientApi.add(client1);
        clientApi.add(client2);
        List<Client> clientList = clientApi.searchByName("test");
        assertEquals(2,clientList.size());
        clientList=clientApi.searchByName("testcl");
        assertEquals(1,clientList.size());
    }
    
    @Test
    public void testUpdateClient() {
        clientApi.add(client1);
        newClient1 = new Client();
        newClient1.setName("updatedName");
        newClient1.setType(ClientType.CUSTOMER);

        Client clientUpdate = clientApi.update(client1.getId(),newClient1);
        assertNotNull(clientUpdate);
        assertEquals("updatedname", clientUpdate.getName());
        assertEquals(ClientType.CUSTOMER,clientUpdate.getType());
    }

    

    @Test
    public void testGetAll() {
        clientApi.add(client1);
        clientApi.add(client2);
        List<Client> list = clientApi.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testGetAllClients() {
        clientApi.add(client1);
        clientApi.add(client2);
        List<Client> list = clientApi.getAllClients();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetAllCustomers() {
        clientApi.add(client1);
        clientApi.add(client2);
        List<Client> list = clientApi.getAllCustomers();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test(expected = ApiException.class)
    public void testGetCheck(){
        clientApi.add(client1);
        clientApi.add(client2);
        Client client = clientApi.getCheck(client1.getId());
        assertNotNull(client);
        assertEquals(client1.getId(), client.getId());
        assertEquals(StringUtil.toLowerCase(client1.getName()), client.getName());
        assertEquals(client1.getType(), client.getType());
        // throw exception
        clientApi.getCheck(client2.getId()+1);
    }

    @Test(expected = ApiException.class)
    public void testGetCheckExisting(){
        clientApi.add(client1);
        clientApi.add(client2);
        clientApi.getCheckExisting("a",ClientType.CLIENT);
        // throw exception if already exist
        clientApi.getCheckExisting(StringUtil.toLowerCase(client1.getName()),ClientType.CLIENT);
    }
    
}
