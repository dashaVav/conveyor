package com.example.conveyor.controllers;

import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/conveyor")
public interface ConveyorController {
    @PostMapping(path = "/offers")
    ResponseEntity<List<LoanOfferDTO>> offers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PostMapping(path = "/calculation")
    ResponseEntity<CreditDTO> calculation(@RequestBody ScoringDataDTO scoringData);
}
