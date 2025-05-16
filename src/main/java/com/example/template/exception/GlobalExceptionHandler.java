package com.example.template.exception;

import com.example.template.util.ResponseUtil;
import com.example.template.util.enums.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice // @ControllerAdvice + @ResponseBody (JSON 응답 반환), 모든 컨트롤러에서 발생하는 예외를 JSON 형태로 반환.
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthenticationException.class,
            com.example.NPKI.exception.UnauthorizedException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            NoSuchElementException.class,
            MissingRequestHeaderException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            HttpClientErrorException.BadRequest.class,
            NoHandlerFoundException.class,
            NullPointerException.class,
            IOException.class,
            EntityExistsException.class,
            JsonParseException.class,
            JsonProcessingException.class,
            DuplicateKeyException.class,
            NoSuchElementException.class,
            MethodArgumentNotValidException.class,
            com.example.NPKI.exception.MissingRequestParameterException.class,
            com.example.NPKI.exception.JwtTokenExpiredException.class,
            com.example.NPKI.exception.JwtTokenIsNotValidException.class,
            com.example.NPKI.exception.InvalidPasswordException.class,
            com.example.NPKI.exception.NoSuchUserException.class
    })
    protected ResponseEntity<ErrorResponse> handleSpecificExceptions(Exception ex) {
        log.error("Exception caught: {}", ex.getClass().getSimpleName(), ex);
        return ResponseUtil.handleException(ex);
    }
}