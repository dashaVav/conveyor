package com.example.conveyor.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanApplicationRequestDTO(
        BigDecimal amount,
        Integer term,
        String firstName,
        String lastName,
        String middleName,
        String email,
        LocalDate birthdate,
        String passportSeries,
        String passportNumber

) {
}