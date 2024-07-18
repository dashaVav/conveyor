package com.example.conveyor.service;

import com.example.conveyor.dto.enums.EmploymentStatus;
import com.example.conveyor.dto.enums.Gender;
import com.example.conveyor.dto.enums.MaritalStatus;
import com.example.conveyor.dto.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RateCalculationService {
    BigDecimal viaGender(BigDecimal rate, Gender gender, LocalDate birthdate);

    BigDecimal viaAge(BigDecimal rate, LocalDate birthdate);

    BigDecimal viaWorkExperience(BigDecimal rate, Integer workExperienceTotal, Integer workExperienceCurrent);

    BigDecimal viaDependentAmount(BigDecimal rate, Integer dependentAmount);

    BigDecimal viaMaritalStatus(BigDecimal rate, MaritalStatus maritalStatus);

    BigDecimal viaSalary(BigDecimal rate, BigDecimal salary, BigDecimal amount);

    BigDecimal viaEmploymentStatus(BigDecimal rate, EmploymentStatus employmentStatus);

    BigDecimal viaPosition(BigDecimal rate, Position position);

    BigDecimal viaSalaryClient(BigDecimal rate, Boolean isSalaryClient);

    BigDecimal viaInsurance(BigDecimal rate, Boolean isInsuranceEnabled);

    BigDecimal getMonthlyPercentageInShapes(BigDecimal rate);

    BigDecimal getRateForTotalPeriod(BigDecimal rate, Integer term);
}
