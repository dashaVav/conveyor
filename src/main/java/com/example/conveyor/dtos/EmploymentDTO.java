package com.example.conveyor.dtos;

import java.math.BigDecimal;

public record EmploymentDTO(
        EmploymentStatus employmentStatus,
        String employerINN,
        BigDecimal salary,
        Position position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {
}
