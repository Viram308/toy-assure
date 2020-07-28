package com.commons.form;

import com.commons.enums.ClientType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientForm {
    private String name;
    private ClientType type;
}
