package com.assure.util;

import com.assure.model.form.ClientForm;
import com.assure.pojo.Client;

public class ConverterUtil {

    public static Client convertClientFormToClient(ClientForm clientForm){
        Client client = new Client();
        client.setName(clientForm.getName());
        client.setType(clientForm.getType());
        return client;
    }
}
