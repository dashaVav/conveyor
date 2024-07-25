package com.example.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConveyorException {
    private String error;
    private Integer status;
}
