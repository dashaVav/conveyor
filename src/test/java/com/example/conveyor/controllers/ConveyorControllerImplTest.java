package com.example.conveyor.controllers;

import com.example.conveyor.controllers.impl.ConveyorControllerImpl;
import com.example.conveyor.dtos.CreditDTO;
import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.LoanOfferDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import com.example.conveyor.services.ScoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConveyorControllerImplTest {
    @InjectMocks
    private ConveyorControllerImpl conveyorController;

    @Mock
    private ScoringService scoringService;

    @Test
    public void testOffersEndpointOk() {
        BigDecimal amount = new BigDecimal(10000);
        Integer term = 6;

        LoanApplicationRequestDTO request = LoanApplicationRequestDTO
                .builder()
                .amount(amount)
                .term(term)
                .build();

        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        loanOffers.add(LoanOfferDTO
                .builder()
                .requestedAmount(amount)
                .term(term)
                .build()
        );
        loanOffers.add(LoanOfferDTO
                .builder()
                .requestedAmount(amount)
                .term(term)
                .build()
        );
        loanOffers.add(LoanOfferDTO
                .builder()
                .requestedAmount(amount)
                .term(term)
                .build()
        );
        loanOffers.add(LoanOfferDTO
                .builder()
                .requestedAmount(amount)
                .term(term)
                .build()
        );

        when(scoringService.createLoanOffers(request)).thenReturn(loanOffers);

        ResponseEntity<List<LoanOfferDTO>> result = conveyorController.offers(request);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(result.getBody()).size()).isEqualTo(loanOffers.size());
        assertThat(result.getBody().getFirst().getRequestedAmount()).isEqualTo(amount);
        assertThat(result.getBody().getFirst().getTerm()).isEqualTo(term);
    }

    @Test
    public void testCalculationsEndpointOk() {
        BigDecimal amount = new BigDecimal(10000);
        Integer term = 6;
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.
                builder()
                .amount(amount)
                .term(term)
                .build();
        CreditDTO creditDTO = CreditDTO
                .builder()
                .amount(amount)
                .term(term)
                .build();

        when(scoringService.createPersonalCreditOffer(scoringDataDTO)).thenReturn(creditDTO);

        ResponseEntity<CreditDTO> result = conveyorController.calculation(scoringDataDTO);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(result.getBody()).getAmount()).isEqualTo(amount);
        assertThat(result.getBody().getTerm()).isEqualTo(term);
    }
}
