package com.example.conveyor.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoanRefusalException.class)
    public ResponseEntity<ConveyorError> handleForbiddenException(RuntimeException e) {
        log.error("Exception: {} handled normally. Message: {}", e.getClass().getName(), e.getMessage());
        return new ResponseEntity<>(
                new ConveyorError(e.getMessage(), HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN
        );
    }
}
