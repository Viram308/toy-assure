package com.commons.api;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class CustomValidationException extends RuntimeException {

    private final Errors errors;
    private final List<FieldError> errorList = new ArrayList<>();

    public CustomValidationException(Errors errors){
        this.errors = errors;
    }

    public List<FieldError> getFormattedErrors() {
        if (errors != null) {
            for (ObjectError objectError : errors.getAllErrors()) {
                if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    errorList.add(fieldError);
                }
            }
        }
        return errorList;
    }
}
