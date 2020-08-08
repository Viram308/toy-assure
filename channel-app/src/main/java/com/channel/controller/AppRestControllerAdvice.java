package com.channel.controller;

import com.commons.api.ApiException;
import com.commons.api.CustomErrorResponse;
import com.commons.api.CustomValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class AppRestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handle(Throwable e) {
        CustomErrorResponse apiError = new CustomErrorResponse(INTERNAL_SERVER_ERROR);
        apiError.setMessage("An unknown internal error has occurred - " + e.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CustomValidationException.class)
    protected ResponseEntity<Object> handleCustomConstraintViolation(
            CustomValidationException ex) {
        CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getFormattedErrors());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(ApiException ex) {
        CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    private ResponseEntity<Object> buildResponseEntity(CustomErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}