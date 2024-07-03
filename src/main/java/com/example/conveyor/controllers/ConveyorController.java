package com.example.conveyor.controllers;

import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import com.example.conveyor.services.PrescoringService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping(path = "/offers")
    public List<LoanOfferDTO> offers(@RequestBody LoanApplicationRequestDTO applicationRequest) {
        prescoringService.isApplicationRequestValid(applicationRequest);
        return null;
    }

    @PostMapping(path = "/calculation")
    public CreditDTO calculation(@RequestBody ScoringDataDTO scoringData) {
        return null;
    }
}
