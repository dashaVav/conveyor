package com.example.conveyor.controller;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/conveyor")
public interface ConveyorController {
    @PostMapping(path = "/offers")
    ResponseEntity<List<LoanOfferDTO>> offers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PostMapping(path = "/calculation")
    ResponseEntity<CreditDTO> calculation(@Valid @RequestBody ScoringDataDTO scoringData);
}
