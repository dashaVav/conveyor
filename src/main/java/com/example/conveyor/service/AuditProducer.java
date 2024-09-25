package com.example.conveyor.service;

import com.example.conveyor.dto.AuditActionDTO;

public interface AuditProducer {
    void produceAuditAction(AuditActionDTO auditAction);
}
