package com.example.conveyor.services;

import java.math.BigDecimal;

public interface AmountCalculationService {
    BigDecimal getAmountViaInsurance(BigDecimal amount, Boolean isInsuranceEnabled, BigDecimal insurance);

    BigDecimal getTotalAmountViaRate(BigDecimal amount, BigDecimal rate, Integer term);

    BigDecimal getMonthlyPayment(BigDecimal totalAmount, Integer term);
}
