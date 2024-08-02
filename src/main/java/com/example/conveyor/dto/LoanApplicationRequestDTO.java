package com.example.conveyor.dto;

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
public class LoanApplicationRequestDTO {
    @NotNull
    private BigDecimal amount;

    private Integer term;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;

    @NotBlank
    private String email;

    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String passportSeries;

    @NotBlank
    private String passportNumber;
}