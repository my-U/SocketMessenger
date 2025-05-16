package com.example.template.exception;

public class JwtTokenIsNotValidException extends RuntimeException {
    public JwtTokenIsNotValidException(String message) {
        super(message);
    }
}
