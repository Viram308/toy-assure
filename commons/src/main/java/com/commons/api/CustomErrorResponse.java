package com.commons.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomErrorResponse {

    private HttpStatus status;
    private String message;
    private List<ApiValidationError> subErrors;

    public CustomErrorResponse(HttpStatus status) {
        this.status = status;
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        ApiValidationError apiValidationError = new ApiValidationError(object, field, rejectedValue, message);
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(apiValidationError);
    }


    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }


}
