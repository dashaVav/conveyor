package com.example.conveyor.controllers;

import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import com.example.conveyor.services.PrescoringService;
import com.example.conveyor.services.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
public class ConveyorController {
    private final PrescoringService prescoringService;
    private final ScoringService scoringService;

    @PostMapping(path = "/offers")
    public ResponseEntity<List<LoanOfferDTO>> offers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        prescoringService.validationOfLoanApplicationRequest(loanApplicationRequest);
        return ResponseEntity.ok(scoringService.createLoanOffers(loanApplicationRequest));
    }

    @PostMapping(path = "/calculation")
    public ResponseEntity<CreditDTO> calculation(@RequestBody ScoringDataDTO scoringData) {
        prescoringService.validationOfScoringData(scoringData);
        return ResponseEntity.ok(scoringService.createCreditOffer(scoringData));
    }
}
