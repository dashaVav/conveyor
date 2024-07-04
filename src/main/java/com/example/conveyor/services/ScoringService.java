package com.example.conveyor.services;

import com.example.conveyor.dtos.*;
import com.example.conveyor.utils.LoanOfferDTOComparator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScoringService {
    private final RateCalculationService rateCalculationService;
    private final AmountCalculationService totalAmountCalculationService;
    private final static BigDecimal INSURANCE = BigDecimal.valueOf(10000);
    private final static BigDecimal STANDARD_RATE = BigDecimal.valueOf(12);

    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        loanOffers.add(createOffer(false, false, loanApplicationRequest));
        loanOffers.add(createOffer(false, true, loanApplicationRequest));
        loanOffers.add(createOffer(true, false, loanApplicationRequest));
        loanOffers.add(createOffer(true, true, loanApplicationRequest));
        return loanOffers.stream().sorted(new LoanOfferDTOComparator()).collect(Collectors.toList());
    }

    private LoanOfferDTO createOffer(Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient,
                                     LoanApplicationRequestDTO applicationRequest) {
        BigDecimal personalRate = rateCalculationService.viaSalaryClient(STANDARD_RATE, isSalaryClient);
        personalRate = rateCalculationService.viaInsurance(personalRate, isInsuranceEnabled);

        BigDecimal totalAmount = totalAmountCalculationService
                .getAmountViaInsurance(applicationRequest.getAmount(), isInsuranceEnabled, INSURANCE);
        totalAmount = totalAmountCalculationService
                .getTotalAmountViaRate(totalAmount, personalRate, applicationRequest.getTerm());

        return LoanOfferDTO
                .builder()
                .applicationId(Math.abs(new Random().nextLong()))
                .requestedAmount(round(applicationRequest.getAmount()))
                .totalAmount(round(totalAmount))
                .term(applicationRequest.getTerm())
                .monthlyPayment(AmountCalculationService.getMonthlyPayment(totalAmount, applicationRequest.getTerm()))
                .rate(personalRate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }

    public CreditDTO createCreditOffer(ScoringDataDTO scoringData) {
        BigDecimal personalRate = calculateRate(scoringData);

        BigDecimal totalAmount = totalAmountCalculationService.getAmountViaInsurance(scoringData.amount(), scoringData.isInsuranceEnabled(), INSURANCE);
        totalAmount = totalAmountCalculationService.getTotalAmountViaRate(totalAmount, personalRate, scoringData.term());

        BigDecimal monthlyPayment = AmountCalculationService.getMonthlyPayment(totalAmount, scoringData.term());
        return CreditDTO
                .builder()
                .amount(round(scoringData.amount()))
                .term(scoringData.term())
                .monthlyPayment(monthlyPayment)
                .rate(personalRate)
                .psk(round(totalAmount))
                .isInsuranceEnabled(scoringData.isInsuranceEnabled())
                .isSalaryClient(scoringData.isSalaryClient())
                .paymentSchedule(paymentScheduleElements(scoringData.term(), monthlyPayment, personalRate, totalAmount))
                .build();
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

            PaymentScheduleElement element = PaymentScheduleElement
                    .builder()
                    .number(number)
                    .date(loanIssuance)
                    .totalPayment(monthlyPayment)
                    .interestPayment(round(monthlyPayment.multiply(BigDecimal.valueOf(1).subtract(rate))))
                    .debtPayment(round(monthlyPayment.multiply(rate)))
                    .remainingDebt(round(amount))
                    .build();
            paymentScheduleElements.add(element);
        }
        return paymentScheduleElements;
    }

    private BigDecimal calculateRate(ScoringDataDTO scoringData) {
        BigDecimal rate = STANDARD_RATE;
        rate = rateCalculationService.viaGender(rate, scoringData.gender(), scoringData.birthdate());
        rate = rateCalculationService.viaAge(rate, scoringData.birthdate());
        rate = rateCalculationService.viaWorkExperience(rate,
                scoringData.employment().workExperienceTotal(),
                scoringData.employment().workExperienceCurrent());
        rate = rateCalculationService.viaDependentAmount(rate, scoringData.dependentAmount());
        rate = rateCalculationService.viaMaritalStatus(rate, scoringData.maritalStatus());
        rate = rateCalculationService.viaSalary(rate, scoringData.employment().salary(), scoringData.amount());
        rate = rateCalculationService.viaEmploymentStatus(rate, scoringData.employment().employmentStatus());
        rate = rateCalculationService.viaPosition(rate, scoringData.employment().position());
        rate = rateCalculationService.viaInsurance(rate, scoringData.isInsuranceEnabled());
        rate = rateCalculationService.viaSalaryClient(rate, scoringData.isSalaryClient());
        return rate;
    }

    private BigDecimal round(BigDecimal number) {
        return number.setScale(2, RoundingMode.FLOOR);
    }
}
