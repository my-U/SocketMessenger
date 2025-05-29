package com.example.socketmessenger.exception;

public class JwtTokenIsNotValidException extends RuntimeException {
    public JwtTokenIsNotValidException(String message) {
        super(message);
    }
}
