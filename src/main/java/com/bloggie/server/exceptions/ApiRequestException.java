package com.bloggie.server.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiRequestException extends RuntimeException {

    private HttpStatus error;

    public ApiRequestException(String message, HttpStatus error) {
        super(message);
        this.error = error;
    }


}
