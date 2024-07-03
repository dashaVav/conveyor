package com.example.conveyor.controllers;

import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import com.example.conveyor.services.LoanOfferService;
import com.example.conveyor.services.PrescoringService;
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
    private final LoanOfferService loanOfferService;

    @PostMapping(path = "/offers")
    public ResponseEntity<List<LoanOfferDTO>> offers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        prescoringService.validationOfLoanApplicationRequest(loanApplicationRequest);
        return ResponseEntity.ok(loanOfferService.createLoanOffers(loanApplicationRequest));
    }

    @PostMapping(path = "/calculation")
    public CreditDTO calculation(@RequestBody ScoringDataDTO scoringData) {
        return null;
    }
}
