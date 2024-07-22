package com.example.conveyor.service.impl;

import com.example.conveyor.dto.*;
import com.example.conveyor.service.AmountCalculationService;
import com.example.conveyor.service.RateCalculationService;
import com.example.conveyor.service.ScoringService;
import com.example.conveyor.utils.LoanOfferDTOComparator;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoringServiceImpl implements ScoringService {
    private final RateCalculationService rateCalculationService;
    private final AmountCalculationService amountCalculationService;
    private static BigDecimal INSURANCE;
    private static BigDecimal STANDARD_RATE;

    public ScoringServiceImpl(RateCalculationService rateCalculationService,
                              AmountCalculationService amountCalculationService) {
        this.amountCalculationService = amountCalculationService;
        this.rateCalculationService = rateCalculationService;
        readDataForLoan();
    }

    private void readDataForLoan() {
        try {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "data_for_loan.json");
            JSONObject dataForLoan = new JSONObject(
                    new String(Files.readAllBytes(file.toPath()))
            );

            STANDARD_RATE = new BigDecimal(dataForLoan.getString("rate"));
            INSURANCE = new BigDecimal(dataForLoan.getString("insurance"));
        } catch (Exception e) {
            STANDARD_RATE = BigDecimal.valueOf(20);
            INSURANCE = BigDecimal.valueOf(10000);
        }
    }

    @Override
    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        loanOffers.add(createOneOffer(false, false, loanApplicationRequest));
        loanOffers.add(createOneOffer(false, true, loanApplicationRequest));
        loanOffers.add(createOneOffer(true, false, loanApplicationRequest));
        loanOffers.add(createOneOffer(true, true, loanApplicationRequest));
        return loanOffers.stream().sorted(new LoanOfferDTOComparator()).collect(Collectors.toList());
    }

    private LoanOfferDTO createOneOffer(Boolean isInsuranceEnabled,
                                        Boolean isSalaryClient,
                                        LoanApplicationRequestDTO applicationRequest) {
        BigDecimal personalRate = rateCalculationService.viaSalaryClient(STANDARD_RATE, isSalaryClient);
        personalRate = rateCalculationService.viaInsurance(personalRate, isInsuranceEnabled);

        BigDecimal totalAmount = calculateTotalAmount(
                applicationRequest.getAmount(),
                isInsuranceEnabled,
                personalRate,
                applicationRequest.getTerm());

        return new LoanOfferDTO()
                .setRequestedAmount(round(applicationRequest.getAmount()))
                .setTotalAmount(round(totalAmount))
                .setTerm(applicationRequest.getTerm())
                .setMonthlyPayment(amountCalculationService.getMonthlyPayment(totalAmount, applicationRequest.getTerm()))
                .setRate(personalRate)
                .setIsInsuranceEnabled(isInsuranceEnabled)
                .setIsSalaryClient(isSalaryClient);
    }

    private BigDecimal calculateTotalAmount(BigDecimal amount,
                                            Boolean isInsuranceEnabled,
                                            BigDecimal personalRate,
                                            Integer term) {
        BigDecimal totalAmount = amountCalculationService.getAmountViaInsurance(amount, isInsuranceEnabled, INSURANCE);
        return amountCalculationService.getTotalAmountViaRate(totalAmount, personalRate, term);
    }

    @Override
    public CreditDTO createPersonalCreditOffer(ScoringDataDTO scoringData) {
        BigDecimal personalRate = calculateRateForPersonalOffer(scoringData);

        BigDecimal totalAmount = calculateTotalAmount(
                scoringData.getAmount(),
                scoringData.getIsInsuranceEnabled(),
                personalRate,
                scoringData.getTerm());

        BigDecimal monthlyPayment = amountCalculationService.getMonthlyPayment(totalAmount, scoringData.getTerm());
        return new CreditDTO()
                .setAmount(round(scoringData.getAmount()))
                .setTerm(scoringData.getTerm())
                .setMonthlyPayment(monthlyPayment)
                .setRate(personalRate)
                .setPsk(round(totalAmount))
                .setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .setIsSalaryClient(scoringData.getIsSalaryClient())
                .setPaymentSchedule(paymentScheduleElements(scoringData.getTerm(), monthlyPayment, personalRate, totalAmount));
    }

    private List<PaymentScheduleElement> paymentScheduleElements(Integer term,
                                                                 BigDecimal monthlyPayment,
                                                                 BigDecimal rate,
                                                                 BigDecimal amount) {
        List<PaymentScheduleElement> paymentScheduleElements = new LinkedList<>();
        LocalDate loanIssuance = LocalDate.now();
        rate = rateCalculationService.getMonthlyPercentageInShapes(rate);

        for (int number = 1; number <= term; number++) {
            amount = amount.subtract(monthlyPayment);
            loanIssuance = loanIssuance.plusMonths(1);
            if (number == term) {
                monthlyPayment = monthlyPayment.add(amount);
                amount = BigDecimal.valueOf(0);
            }

            PaymentScheduleElement element = new PaymentScheduleElement()
                    .setNumber(number)
                    .setDate(loanIssuance)
                    .setTotalPayment(round(monthlyPayment))
                    .setInterestPayment(round(monthlyPayment.multiply(BigDecimal.valueOf(1).subtract(rate))))
                    .setDebtPayment(round(monthlyPayment.multiply(rate)))
                    .setRemainingDebt(round(amount));
            paymentScheduleElements.add(element);
        }
        return paymentScheduleElements;
    }

    private BigDecimal calculateRateForPersonalOffer(ScoringDataDTO scoringData) {
        BigDecimal rate = STANDARD_RATE;
        rate = rateCalculationService.viaGender(rate, scoringData.getGender(), scoringData.getBirthdate());
        rate = rateCalculationService.viaAge(rate, scoringData.getBirthdate());
        rate = rateCalculationService.viaWorkExperience(rate,
                scoringData.getEmployment().getWorkExperienceTotal(),
                scoringData.getEmployment().getWorkExperienceCurrent());
        rate = rateCalculationService.viaDependentAmount(rate, scoringData.getDependentAmount());
        rate = rateCalculationService.viaMaritalStatus(rate, scoringData.getMaritalStatus());
        rate = rateCalculationService.viaSalary(rate, scoringData.getEmployment().getSalary(), scoringData.getAmount());
        rate = rateCalculationService.viaEmploymentStatus(rate, scoringData.getEmployment().getEmploymentStatus());
        rate = rateCalculationService.viaPosition(rate, scoringData.getEmployment().getPosition());
        rate = rateCalculationService.viaInsurance(rate, scoringData.getIsInsuranceEnabled());
        rate = rateCalculationService.viaSalaryClient(rate, scoringData.getIsSalaryClient());
        return rate;
    }

    private BigDecimal round(BigDecimal number) {
        return number.setScale(2, RoundingMode.UP);
    }
}
