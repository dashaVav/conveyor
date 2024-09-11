package com.example.conveyor.dto;

import com.example.conveyor.dto.enums.ServiceDTO;
import com.example.conveyor.dto.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditActionDTO implements Serializable {
    private UUID id;
    private Type type;
    private ServiceDTO service;
    private String message;
}
