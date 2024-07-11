package com.example.conveyor.controllers.impl;

import com.example.conveyor.controllers.ConveyorController;
import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import com.example.conveyor.services.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConveyorControllerImpl implements ConveyorController {
    private final ScoringService scoringService;

    public ResponseEntity<List<LoanOfferDTO>> offers(LoanApplicationRequestDTO loanApplicationRequest) {
        return ResponseEntity.ok(scoringService.createLoanOffers(loanApplicationRequest));
    }

    public ResponseEntity<CreditDTO> calculation(ScoringDataDTO scoringData) {
        return ResponseEntity.ok(scoringService.createPersonalCreditOffer(scoringData));
    }
}
