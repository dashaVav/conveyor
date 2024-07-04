package com.example.conveyor.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class AmountCalculationService {
    private final RateCalculationService rateCalculationService;

    public BigDecimal getAmountViaInsurance(BigDecimal amount, Boolean isInsuranceEnabled, BigDecimal insurance) {
        return isInsuranceEnabled ? amount.add(insurance) : amount;
    }

    public BigDecimal getTotalAmountViaRate(BigDecimal amount, BigDecimal rate, Integer term) {
        return amount.multiply(rateCalculationService.getRateForTotalPeriod(rate, term));
    }

    public static BigDecimal getMonthlyPayment(BigDecimal totalAmount, Integer term) {
        return totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.FLOOR);
    }
}
