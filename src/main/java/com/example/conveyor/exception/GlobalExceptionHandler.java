package com.example.conveyor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ConveyorException> handleTheException(RuntimeException e, HttpStatus status) {
        return new ResponseEntity<>(
                new ConveyorException(e.getMessage(), status.value()),
                status
        );
    }

    @ExceptionHandler(PrescoringException.class)
    public ResponseEntity<ConveyorException> handleBadRequestsException(RuntimeException e) {
        return handleTheException(e, HttpStatus.BAD_REQUEST);
    }
}
