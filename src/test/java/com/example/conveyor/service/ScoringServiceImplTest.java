package com.example.conveyor.service;

import com.example.conveyor.dto.*;
import com.example.conveyor.dto.enums.EmploymentStatus;
import com.example.conveyor.dto.enums.Gender;
import com.example.conveyor.dto.enums.MaritalStatus;
import com.example.conveyor.dto.enums.Position;
import com.example.conveyor.service.impl.AmountCalculationServiceImpl;
import com.example.conveyor.service.impl.RateCalculationServiceImpl;
import com.example.conveyor.service.impl.ScoringServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoringServiceImplTest {
    private ScoringServiceImpl scoringService;

    @BeforeEach
    void setUp() {
        RateCalculationService rateCalculationService = new RateCalculationServiceImpl();
        AmountCalculationService amountCalculationService = new AmountCalculationServiceImpl(rateCalculationService);
        scoringService = new ScoringServiceImpl(rateCalculationService, amountCalculationService);
    }

    private static Stream<Arguments> isInsuranceEnabledAndIsSalaryClientProvider() {
        return Stream.of(
                Arguments.of(false, false),
                Arguments.of(false, true),
                Arguments.of(true, false),
                Arguments.of(true, true)
        );
    }

    @ParameterizedTest
    @MethodSource("isInsuranceEnabledAndIsSalaryClientProvider")
    public void testCreateLoanOffers(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        BigDecimal loanAmount = new BigDecimal("10000.00");
        int term = 6;
        LoanApplicationRequestDTO applicationRequest = new LoanApplicationRequestDTO();
        applicationRequest.setAmount(loanAmount).setTerm(term);

        List<LoanOfferDTO> loanOffers = scoringService.createLoanOffers(applicationRequest);
        assertEquals(loanOffers.size(), 4);
        loanOffers.forEach(loanOffer -> {
            assertEquals(loanOffer.getTerm(), term);
            assertEquals(loanOffer.getRequestedAmount(), loanAmount);
        });

        for (int i = 0; i < 3; i++) {
            assertTrue(loanOffers.get(i).getRate().compareTo(loanOffers.get(i + 1).getRate()) > 0);
        }

        assertEquals(loanOffers.stream().filter(offer -> offer.getIsInsuranceEnabled().equals(isInsuranceEnabled) && offer.getIsSalaryClient().equals(isSalaryClient)).count(),
                1);
    }

    @Test
    public void testCreatePersonalCreditOffer() {
        BigDecimal amount = new BigDecimal("10000.00");
        int term = 6;
        ScoringDataDTO scoringData = new ScoringDataDTO()
                .setAmount(amount)
                .setTerm(term)
                .setBirthdate(LocalDate.of(2002, 10, 11))
                .setGender(Gender.FEMALE)
                .setEmployment(
                        new EmploymentDTO()
                                .setWorkExperienceCurrent(4)
                                .setWorkExperienceTotal(13)
                                .setSalary(BigDecimal.valueOf(10000))
                                .setEmploymentStatus(EmploymentStatus.EMPLOYED)
                                .setPosition(Position.WORKER)
                )
                .setDependentAmount(1)
                .setMaritalStatus(MaritalStatus.MARRIED)
                .setIsSalaryClient(true)
                .setIsInsuranceEnabled(true);

        CreditDTO credit = scoringService.createPersonalCreditOffer(scoringData);

        assertEquals(credit.getAmount(), amount);
        assertEquals(credit.getTerm(), term);
    }
}
