package com.example.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConveyorError {
    private String error;
    private Integer status;
}
