package com.example.conveyor.service;

import java.math.BigDecimal;

public interface AmountCalculationService {
    BigDecimal getAmountViaInsurance(BigDecimal amount, boolean isInsuranceEnabled, BigDecimal insurance);

    BigDecimal getTotalAmountViaRate(BigDecimal amount, BigDecimal rate, Integer term);

    BigDecimal getMonthlyPayment(BigDecimal totalAmount, Integer term);
}
