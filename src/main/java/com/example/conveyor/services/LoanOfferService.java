package com.example.conveyor.services;

import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.utils.LoanOfferDTOComparator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanOfferService {
    private final static BigDecimal insurance = BigDecimal.valueOf(10000);
    private final static BigDecimal rate = BigDecimal.valueOf(12);

    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        loanOffers.add(createOffer(false, false, loanApplicationRequest));
        loanOffers.add(createOffer(false, true, loanApplicationRequest));
        loanOffers.add(createOffer(true, false, loanApplicationRequest));
        loanOffers.add(createOffer(true, true, loanApplicationRequest));
        return loanOffers.stream().sorted(new LoanOfferDTOComparator()).collect(Collectors.toList());
    }

    private LoanOfferDTO createOffer(Boolean isInsuranceEnabled, Boolean isSalaryClient, LoanApplicationRequestDTO request) {
        BigDecimal personalRate = calculateRateViaSalaryClient(rate, isSalaryClient);
        personalRate = calculateRateViaInsurance(personalRate, isInsuranceEnabled).setScale(2, RoundingMode.FLOOR);

        BigDecimal totalAmount = calculateTotalAmountViaInsurance(request.getAmount(), isInsuranceEnabled);
        totalAmount = calculateFinalTotalAmountViaRate(totalAmount, personalRate);

        return LoanOfferDTO
                .builder()
                .applicationId(System.currentTimeMillis())
                .requestedAmount(request.getAmount())
                .totalAmount(totalAmount)
                .term(request.getTerm())
                .monthlyPayment(calculateMonthlyPayment(totalAmount, request.getTerm()))
                .rate(personalRate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }

    private BigDecimal calculateRateViaSalaryClient(BigDecimal personalRate, Boolean isSalaryClient) {
        if (isSalaryClient) {
            personalRate = personalRate.subtract(BigDecimal.valueOf(0.3));
        }
        return personalRate;
    }

    private BigDecimal calculateRateViaInsurance(BigDecimal personalRate, Boolean isInsuranceEnabled) {
        if (isInsuranceEnabled) {
            personalRate = personalRate.subtract(BigDecimal.valueOf(3));
        } else {
            personalRate = personalRate.add(BigDecimal.valueOf(1));
        }
        return personalRate;
    }

    private BigDecimal calculateTotalAmountViaInsurance(BigDecimal totalAmount, Boolean isInsuranceEnabled) {
        return isInsuranceEnabled ? totalAmount.add(insurance) : totalAmount;
    }

    private BigDecimal calculateFinalTotalAmountViaRate(BigDecimal totalAmount, BigDecimal personalRate) {
        BigDecimal rateAsPercentage = personalRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP).add(BigDecimal.valueOf(1));
        return totalAmount.multiply(rateAsPercentage);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term) {
        return totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
    }
}
