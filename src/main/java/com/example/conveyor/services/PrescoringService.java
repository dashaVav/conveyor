package com.example.conveyor.services;

import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.ScoringDataDTO;

public interface PrescoringService {
    void validationOfLoanApplicationRequest(LoanApplicationRequestDTO loanApplicationRequest);

    void validationOfScoringData(ScoringDataDTO scoringData);
}
