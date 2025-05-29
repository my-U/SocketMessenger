package com.example.socketmessenger.exception;

public class MissingRequestParameterException extends RuntimeException {
    public MissingRequestParameterException(String message) {
        super(message);
    }
}
