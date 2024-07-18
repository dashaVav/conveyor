package com.example.conveyor.controller.impl;

import com.example.conveyor.controller.ConveyorController;
import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.service.ScoringService;
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
