package com.example.conveyor.utils;

import com.example.conveyor.dto.LoanOfferDTO;

import java.util.Comparator;

public class LoanOfferDTOComparator implements Comparator<LoanOfferDTO> {
    public int compare(LoanOfferDTO a, LoanOfferDTO b) {
        return b.getRate().compareTo(a.getRate());
    }
}
