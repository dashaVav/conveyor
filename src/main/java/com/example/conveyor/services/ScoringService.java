package com.example.conveyor.services;

import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;

import java.util.List;

public interface ScoringService {
    List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

    CreditDTO createCreditOffer(ScoringDataDTO scoringData);
}
