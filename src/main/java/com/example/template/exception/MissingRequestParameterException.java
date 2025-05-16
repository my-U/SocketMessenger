package com.example.template.exception;

public class MissingRequestParameterException extends RuntimeException {
    public MissingRequestParameterException(String message) {
        super(message);
    }
}
