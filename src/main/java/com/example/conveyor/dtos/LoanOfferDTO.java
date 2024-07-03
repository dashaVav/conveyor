package com.example.conveyor.dtos;

import java.math.BigDecimal;

public record LoanOfferDTO(
        Long applicationId,
        BigDecimal requestedAmount,
        BigDecimal totalAmount,
        Integer term,
        BigDecimal monthlyPayment,
        BigDecimal rate,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient
) {
}