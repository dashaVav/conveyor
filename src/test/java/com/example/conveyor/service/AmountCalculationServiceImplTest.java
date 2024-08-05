package com.example.conveyor.service;

import com.example.conveyor.service.impl.AmountCalculationServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AmountCalculationServiceImplTest {

    private static AmountCalculationServiceImpl amountCalculationService;

    @Mock
    private static RateCalculationService rateCalculationService;

    @BeforeAll
    static void setUp() {
        rateCalculationService = Mockito.mock(RateCalculationService.class);
        amountCalculationService = new AmountCalculationServiceImpl(rateCalculationService);
    }

    private static Stream<Arguments> getAmountViaInsuranceProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(10000), true, BigDecimal.valueOf(1000), BigDecimal.valueOf(11000)),
                Arguments.of(BigDecimal.valueOf(10000), false, BigDecimal.valueOf(1000), BigDecimal.valueOf(10000))
        );
    }

    @ParameterizedTest
    @MethodSource("getAmountViaInsuranceProvider")
    void testGetAmountViaInsurance(BigDecimal amount,
                                   Boolean isInsuranceEnabled,
                                   BigDecimal insurance,
                                   BigDecimal expectedAmount
    ) {
        BigDecimal result = amountCalculationService.getAmountViaInsurance(amount, isInsuranceEnabled, insurance);
        assertEquals(expectedAmount, result);
    }

    @Test
    void testGetTotalAmountViaRate() {
        BigDecimal rate = BigDecimal.valueOf(10);
        Integer term = 6;

        when(rateCalculationService.getRateForTotalPeriod(rate, term)).thenReturn(BigDecimal.valueOf(1.1));

        BigDecimal result = amountCalculationService.getTotalAmountViaRate(BigDecimal.valueOf(10000), rate, term);

        assertEquals(BigDecimal.valueOf(11000.0), result);
    }

    @Test
    void testGetMonthlyPayment() {
        BigDecimal result = amountCalculationService.getMonthlyPayment(BigDecimal.valueOf(12000), 12);
        assertEquals(new BigDecimal("1000.00"), result);
    }
}
