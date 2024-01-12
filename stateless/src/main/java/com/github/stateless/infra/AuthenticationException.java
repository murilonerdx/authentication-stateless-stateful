package com.github.stateless.infra;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthenticationException extends
Exception{
    public AuthenticationException(String message) {
        super(message);
    }
}
