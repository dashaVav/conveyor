package com.example.conveyor.service.impl;

import com.example.conveyor.service.AmountCalculationService;
import com.example.conveyor.service.RateCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class AmountCalculationServiceImpl implements AmountCalculationService {
    private final RateCalculationService rateCalculationService;

    @Override
    public BigDecimal getAmountViaInsurance(BigDecimal amount, Boolean isInsuranceEnabled, BigDecimal insurance) {
        return isInsuranceEnabled ? amount.add(insurance) : amount;
    }

    @Override
    public BigDecimal getTotalAmountViaRate(BigDecimal amount, BigDecimal rate, Integer term) {
        return amount.multiply(rateCalculationService.getRateForTotalPeriod(rate, term));
    }

    @Override
    public BigDecimal getMonthlyPayment(BigDecimal totalAmount, Integer term) {
        return totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.FLOOR);
    }
}
