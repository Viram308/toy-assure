package com.commons.api;

public class ApiException extends RuntimeException{
    public ApiException(String string) {
        super(string);
    }
}
