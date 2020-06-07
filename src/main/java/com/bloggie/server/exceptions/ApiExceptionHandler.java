package com.bloggie.server.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiException(ApiRequestException ex) {
        ApiException apiException = new ApiException(ex.getMessage(), ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, ex.getError());
    }
}
