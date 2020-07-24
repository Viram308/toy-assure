package com.commons.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}