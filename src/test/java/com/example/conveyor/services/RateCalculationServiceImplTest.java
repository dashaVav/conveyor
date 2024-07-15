package com.example.conveyor.services;

import com.example.conveyor.dtos.EmploymentStatus;
import com.example.conveyor.dtos.Gender;
import com.example.conveyor.dtos.MaritalStatus;
import com.example.conveyor.dtos.Position;
import com.example.conveyor.exceptions.LoanRefusalException;
import com.example.conveyor.services.impl.RateCalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RateCalculationServiceImplTest {

    private final RateCalculationServiceImpl rateCalculationService = new RateCalculationServiceImpl();
    private static final BigDecimal STANDARD_RATE = BigDecimal.valueOf(10);

    private static Stream<Arguments> genderProvider() {
        return Stream.of(
                Arguments.of(Gender.FEMALE, LocalDate.of(1970, 1, 1), BigDecimal.valueOf(7)),
                Arguments.of(Gender.FEMALE, LocalDate.of(1940, 1, 1), BigDecimal.valueOf(10)),
                Arguments.of(Gender.MALE, LocalDate.of(1980, 1, 1), BigDecimal.valueOf(7)),
                Arguments.of(Gender.NON_BINARY, LocalDate.of(1990, 1, 1), BigDecimal.valueOf(13))
        );
    }

    @ParameterizedTest
    @MethodSource("genderProvider")
    public void testRateViaGender(Gender gender, LocalDate birthdate, BigDecimal expectedFinalRate) {
        BigDecimal result = rateCalculationService.viaGender(STANDARD_RATE, gender, birthdate);
        assertEquals(expectedFinalRate, result);
    }

    @Test
    public void testRateViaAgeException() {
        LocalDate birthdate = LocalDate.of(1940, 1, 1);
        assertThrows(LoanRefusalException.class, () -> rateCalculationService.viaAge(STANDARD_RATE, birthdate));
    }

    @Test
    public void testRateViaAge() {
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        assertEquals(rateCalculationService.viaAge(STANDARD_RATE, birthdate), STANDARD_RATE);
    }

    private static Stream<Arguments> workExperienceProvider() {
        return Stream.of(
                Arguments.of(11, 10),
                Arguments.of(13, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("workExperienceProvider")
    public void testRateViaWorkExperienceException(Integer workExperienceTotal, Integer workExperienceCurrent) {
        assertThrows(LoanRefusalException.class, () -> rateCalculationService.viaWorkExperience(STANDARD_RATE, workExperienceTotal, workExperienceCurrent));
    }

    @Test
    public void testRateViaWorkExperience() {
        assertEquals(rateCalculationService.viaWorkExperience(STANDARD_RATE, 13, 4), STANDARD_RATE);
    }

    private static Stream<Arguments> workDependentAmountProvider() {
        return Stream.of(
                Arguments.of(1, STANDARD_RATE),
                Arguments.of(2, STANDARD_RATE.add(BigDecimal.valueOf(1)))
        );
    }

    @ParameterizedTest
    @MethodSource("workDependentAmountProvider")
    public void testRateViaWorkDependentAmount(Integer dependentAmount, BigDecimal expectedRate) {
        assertThat(rateCalculationService.viaDependentAmount(STANDARD_RATE, dependentAmount)).isEqualTo(expectedRate);
    }

    private static Stream<Arguments> viaMaritalStatusProvider() {
        return Stream.of(
                Arguments.of(MaritalStatus.MARRIED, STANDARD_RATE.subtract(BigDecimal.valueOf(3))),
                Arguments.of(MaritalStatus.DIVORCED, STANDARD_RATE.add(BigDecimal.valueOf(1))),
                Arguments.of(MaritalStatus.SINGLE, STANDARD_RATE)
        );
    }

    @ParameterizedTest
    @MethodSource("viaMaritalStatusProvider")
    public void testRateViaMaritalStatus(MaritalStatus maritalStatus, BigDecimal expectedRate) {
        assertThat(rateCalculationService.viaMaritalStatus(STANDARD_RATE, maritalStatus)).isEqualTo(expectedRate);
    }

    private static Stream<Arguments> viaEmploymentStatusProvider() {
        return Stream.of(
                Arguments.of(EmploymentStatus.SELF_EMPLOYED, STANDARD_RATE.add(BigDecimal.valueOf(1))),
                Arguments.of(EmploymentStatus.BUSINESS_OWNER, STANDARD_RATE.add(BigDecimal.valueOf(3))),
                Arguments.of(EmploymentStatus.EMPLOYED, STANDARD_RATE)
        );
    }

    @ParameterizedTest
    @MethodSource("viaEmploymentStatusProvider")
    public void testRateViaEmploymentStatus(EmploymentStatus employmentStatus, BigDecimal expectedRate) {
        assertThat(rateCalculationService.viaEmploymentStatus(STANDARD_RATE, employmentStatus)).isEqualTo(expectedRate);
    }

    @Test
    public void testRateViaEmploymentStatusException() {
        assertThrows(LoanRefusalException.class, () -> rateCalculationService.viaEmploymentStatus(STANDARD_RATE, EmploymentStatus.UNEMPLOYED));
    }

    private static Stream<Arguments> viaPositionProvider() {
        return Stream.of(
                Arguments.of(Position.MID_MANAGER, STANDARD_RATE.subtract(BigDecimal.valueOf(2))),
                Arguments.of(Position.TOP_MANAGER, STANDARD_RATE.subtract(BigDecimal.valueOf(4))),
                Arguments.of(Position.WORKER, STANDARD_RATE)
        );
    }

    @ParameterizedTest
    @MethodSource("viaPositionProvider")
    public void testRateViaPosition(Position position, BigDecimal expectedRate) {
        assertThat(rateCalculationService.viaPosition(STANDARD_RATE, position)).isEqualTo(expectedRate);
    }

    @Test
    public void testRateViaSalaryException() {
        assertThrows(LoanRefusalException.class, () -> rateCalculationService.viaSalary(STANDARD_RATE, BigDecimal.valueOf(10), BigDecimal.valueOf(10000)));
    }

    @Test
    public void testRateViaSalary() {
        assertEquals(rateCalculationService.viaSalary(STANDARD_RATE, BigDecimal.valueOf(1000), BigDecimal.valueOf(10000)), STANDARD_RATE);
    }

    private static Stream<Arguments> viaSalaryClientProvider() {
        return Stream.of(
                Arguments.of(true, STANDARD_RATE.subtract(BigDecimal.valueOf(0.3))),
                Arguments.of(false, STANDARD_RATE)
        );
    }

    @ParameterizedTest
    @MethodSource("viaSalaryClientProvider")
    public void testRateViaSalaryClient(Boolean isSalaryClient, BigDecimal expectedRate) {
        assertThat(rateCalculationService.viaSalaryClient(STANDARD_RATE, isSalaryClient)).isEqualTo(expectedRate);
    }

    private static Stream<Arguments> viaInsuranceProvider() {
        return Stream.of(
                Arguments.of(true, STANDARD_RATE.subtract(BigDecimal.valueOf(3))),
                Arguments.of(false, STANDARD_RATE.add(BigDecimal.valueOf(1)))
        );
    }

    @ParameterizedTest
    @MethodSource("viaInsuranceProvider")
    public void testRateViaInsurance(Boolean isInsuranceEnabled, BigDecimal expectedRate) {
        assertThat(rateCalculationService.viaInsurance(STANDARD_RATE, isInsuranceEnabled)).isEqualTo(expectedRate);
    }

    @Test
    void testGetMonthlyPercentageInShapes() {
        BigDecimal rate = BigDecimal.valueOf(15.5);

        BigDecimal actual = rateCalculationService.getMonthlyPercentageInShapes(rate);

        assertEquals(BigDecimal.valueOf(0.012916), actual);
    }

    @Test
    void testGetRateForTotalPeriod() {
        BigDecimal rate = BigDecimal.valueOf(15.5);
        int term = 24;

        BigDecimal actual = rateCalculationService.getRateForTotalPeriod(rate, term);

        assertEquals(BigDecimal.valueOf(1.309984), actual);
    }


}
