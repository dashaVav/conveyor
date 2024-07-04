package com.example.conveyor.services;

import com.example.conveyor.dtos.EmploymentStatus;
import com.example.conveyor.dtos.Gender;
import com.example.conveyor.dtos.MaritalStatus;
import com.example.conveyor.dtos.Position;
import com.example.conveyor.exception.LoanRefusalException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class RateCalculationService {
    public BigDecimal viaGender(BigDecimal rate, Gender gender, LocalDate birthdate) {
        int age = calculateAge(birthdate);
        BigDecimal changingRate = BigDecimal.valueOf(3);

        if (gender.equals(Gender.WOMAN) && age >= 35 && age < 60) {
            rate = rate.subtract(changingRate);
        } else if (gender.equals(Gender.MAN) && age >= 30 && age < 55) {
            rate = rate.subtract(changingRate);
        } else {
            rate = rate.add(changingRate);
        }

        return rate;
    }

    public BigDecimal viaAge(BigDecimal rate, LocalDate birthdate) {
        if (calculateAge(birthdate) < 20 && calculateAge(birthdate) > 60) {
            throw new LoanRefusalException("The age should be between 20 and 60 years old.");
        }
        return rate;
    }

    private int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    public BigDecimal viaWorkExperience(BigDecimal rate, Integer workExperienceTotal, Integer workExperienceCurrent) {
        if (workExperienceTotal < 12) {
            throw new LoanRefusalException("Total work experience is less than 12 months.");
        }
        if (workExperienceCurrent < 3) {
            throw new LoanRefusalException("Current work experience is less than 3 months.");
        }
        return rate;
    }

    public BigDecimal viaDependentAmount(BigDecimal rate, Integer dependentAmount) {
        if (dependentAmount > 1) {
            rate = rate.add(BigDecimal.valueOf(1));
        }
        return rate;
    }

    public BigDecimal viaMaritalStatus(BigDecimal rate, MaritalStatus maritalStatus) {
        if (maritalStatus.equals(MaritalStatus.MARRIED)) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (maritalStatus.equals(MaritalStatus.DIVORCED)) {
            rate = rate.add(BigDecimal.valueOf(1));
        }
        return rate;
    }

    public BigDecimal viaSalary(BigDecimal rate, BigDecimal salary, BigDecimal amount) {
        if (amount.compareTo(salary.multiply(BigDecimal.valueOf(20))) > 0) {
            throw new LoanRefusalException("Loan amount is more than 20 salaries.");
        }
        return rate;
    }

    public BigDecimal viaEmploymentStatus(BigDecimal rate, EmploymentStatus employmentStatus) {
        switch (employmentStatus) {
            case UNEMPLOYED -> throw new LoanRefusalException("Loan is unemployed");
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(1));
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(3));
        }
        return rate;
    }

    public BigDecimal viaPosition(BigDecimal rate, Position position) {
        if (position.equals(Position.MIDDLE_MANAGER)) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else if (position.equals(Position.TOP_MANAGER)) {
            rate = rate.subtract(BigDecimal.valueOf(4));
        }
        return rate;
    }

    public BigDecimal viaSalaryClient(BigDecimal rate, Boolean isSalaryClient) {
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(0.3));
        }
        return rate;
    }

    public BigDecimal viaInsurance(BigDecimal rate, Boolean isInsuranceEnabled) {
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else {
            rate = rate.add(BigDecimal.valueOf(1));
        }
        return rate;
    }

    public BigDecimal getMonthlyPercentageInShapes(BigDecimal rate) {
        return rate.divide(BigDecimal.valueOf(1200), 6, RoundingMode.FLOOR);
    }

    public BigDecimal getRateForTotalPeriod(BigDecimal rate, Integer term) {
        return getMonthlyPercentageInShapes(rate).multiply(BigDecimal.valueOf(term)).add(BigDecimal.valueOf(1));
    }
}
