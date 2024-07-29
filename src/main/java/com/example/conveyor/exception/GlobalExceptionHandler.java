package com.example.conveyor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoanRefusalException.class)
    public ResponseEntity<ConveyorError> handleForbiddenException(RuntimeException e) {
        return new ResponseEntity<>(
                new ConveyorError(e.getMessage(), HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN
        );
    }
}
