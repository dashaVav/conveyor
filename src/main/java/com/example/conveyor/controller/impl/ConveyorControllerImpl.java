package com.example.conveyor.controller.impl;

import com.example.conveyor.controller.ConveyorController;
import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.service.ScoringService;
import com.example.conveyor.utils.LoggerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConveyorControllerImpl implements ConveyorController {
    private final ScoringService scoringService;

    public ResponseEntity<List<LoanOfferDTO>> offers(LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("/conveyor/offers requested. Body: {}", LoggerUtils.cut(loanApplicationRequest, 100));
        return ResponseEntity.ok(scoringService.createLoanOffers(loanApplicationRequest));
    }

    public ResponseEntity<CreditDTO> calculation(ScoringDataDTO scoringData) {
        log.info("/conveyor/calculation requested. Body: {}", LoggerUtils.cut(scoringData, 100));
        return ResponseEntity.ok(scoringService.createPersonalCreditOffer(scoringData));
    }
}
