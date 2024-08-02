package com.example.conveyor.dto;

import com.example.conveyor.dto.enums.Gender;
import com.example.conveyor.dto.enums.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScoringDataDTO {
    @NotNull
    private BigDecimal amount;

    private Integer term;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String passportSeries;

    @NotBlank
    private String passportNumber;

    @NotNull
    private LocalDate passportIssueDate;

    @NotBlank
    private String passportIssueBranch;

    @NotNull
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @NotNull
    private EmploymentDTO employment;

    @NotBlank
    private String account;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}