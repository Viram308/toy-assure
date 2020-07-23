package com.assure.util;

import com.assure.model.form.ClientForm;
import com.assure.pojo.Client;
import com.commons.enums.ClientType;
import com.commons.util.StringUtil;

public class NormalizeUtil {

    public static void normalizeClient(Client client){
        client.setName(StringUtil.toLowerCase(client.getName()));
        client.setType(Enum.valueOf(ClientType.class,StringUtil.toUpperCase(client.getType().toString())));
    }

}
