package com.gustavo.autocontrato.exception;

import com.gustavo.autocontrato.exception.exceptions.AuthenticationException;
import com.gustavo.autocontrato.exception.exceptions.EventNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionErroMessage> exceptionNotFound(EventNotFoundException exception) {
        ExceptionErroMessage em = new ExceptionErroMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(em);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionErroMessage> handleAuthenticationException(AuthenticationException ex) {
        ExceptionErroMessage errorMessage = new ExceptionErroMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionErroMessage> handleNotFoundException(NoHandlerFoundException ex) {
        ExceptionErroMessage errorResponse = new ExceptionErroMessage(
                HttpStatus.NOT_FOUND,
                "Rota não encontrada",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}