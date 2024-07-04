package com.example.conveyor.services.impl;

import com.example.conveyor.dtos.LoanApplicationRequestDTO;
import com.example.conveyor.dtos.ScoringDataDTO;
import com.example.conveyor.exception.PrescoringException;
import com.example.conveyor.services.PrescoringService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service
public class PrescoringServiceImpl implements PrescoringService {
    @Override
    public void validationOfLoanApplicationRequest(LoanApplicationRequestDTO loanApplicationRequest) {
        validationOfFirstName(loanApplicationRequest.getFirstName());
        validationOfLastName(loanApplicationRequest.getLastName());
        validationOfMiddleName(loanApplicationRequest.getMiddleName());
        validationOfCreditAmount(loanApplicationRequest.getAmount());
        validationOfTerm(loanApplicationRequest.getTerm());
        validationOfBirthdate(loanApplicationRequest.getBirthdate());
        validationOfEmail(loanApplicationRequest.getEmail());
        validationOfPassportSeries(loanApplicationRequest.getPassportSeries());
        validationOfPassportNumber(loanApplicationRequest.getPassportNumber());
    }

    @Override
    public void validationOfScoringData(ScoringDataDTO scoringData) {
        validationOfFirstName(scoringData.firstName());
        validationOfLastName(scoringData.lastName());
        validationOfMiddleName(scoringData.middleName());
        validationOfCreditAmount(scoringData.amount());
        validationOfTerm(scoringData.term());
        validationOfBirthdate(scoringData.birthdate());
        validationOfPassportSeries(scoringData.passportSeries());
        validationOfPassportNumber(scoringData.passportNumber());
    }

    private Boolean isNameValid(String name) {
        return Pattern.compile("^[a-zA-Z]{2,30}$").matcher(name).matches();
    }

    private void validationOfFirstName(String firstName) {
        if (!isNameValid(firstName)) {
            throw new PrescoringException("First name must contain between 2 and 30 Latin letters.");
        }
    }

    private void validationOfLastName(String lastName) {
        if (!isNameValid(lastName)) {
            throw new PrescoringException("Last name must contain between 2 and 30 Latin letters.");
        }
    }

    private void validationOfMiddleName(String middleName) {
        if (middleName != null && !middleName.isEmpty() && !isNameValid(middleName)) {
            throw new PrescoringException("Middle name must contain between 2 and 30 Latin letters.");
        }
    }

    private void validationOfCreditAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(10000)) < 0) {
            throw new PrescoringException("Amount must be number greater than or equal to 10,0000.");
        }
    }

    private void validationOfTerm(Integer term) {
        if (term < 6) {
            throw new PrescoringException("Term must be number greater than or equal to 6.");
        }
    }

    private void validationOfBirthdate(LocalDate birthdate) {
        if (Period.between(birthdate, LocalDate.now()).getYears() < 18) {
            throw new PrescoringException("Client must be over 18 years old.");
        }
    }

    private void validationOfEmail(String email) {
        if (!Pattern.compile("[\\w.]{2,50}@[\\w.]{2,20}").matcher(email).matches()) {
            throw new PrescoringException("Email must match the pattern [\\w.]{2,50}@[\\w.]{2,20}.");
        }
    }

    private void validationOfPassportSeries(String passportSeries) {
        if (!Pattern.compile(String.format("^.{%d}$", 4)).matcher(passportSeries).matches()) {
            throw new PrescoringException("Passport series must contain 4 digits.");
        }
    }

    private void validationOfPassportNumber(String passportNumber) {
        if (!Pattern.compile(String.format("^.{%d}$", 6)).matcher(passportNumber).matches()) {
            throw new PrescoringException("Passport number must contain 6 digits.");
        }
    }
}

