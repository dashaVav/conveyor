package com.example.conveyor.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScoringDataDTO(
        BigDecimal amount,
        Integer term,
        String firstName,
        String lastName,
        String middleName,
        Enum gender,
        LocalDate birthdate,
        String passportSeries,
        String passportNumber,
        LocalDate passportIssueDate,
        String passportIssueBranch,
        Enum maritalStatus,
        Integer dependentAmount,
        EmploymentDTO employment,
        String account,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient
) {
}