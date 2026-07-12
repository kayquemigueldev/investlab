package com.kayque.investlab.domain.strategy;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InterestRateConversionStrategyTest {

    @Test
    void shouldConvertMonthlyPercentageToDecimalRate() {
        MonthlyRateConversionStrategy strategy =
                new MonthlyRateConversionStrategy();

        BigDecimal result =
                strategy.convertToMonthlyRate(
                        new BigDecimal("1.00")
                );

        assertEquals(
                new BigDecimal("0.0100000000000000"),
                result
        );
    }

    @Test
    void shouldConvertNominalAnnualRateToMonthlyRate() {
        AnnualNominalRateConversionStrategy strategy =
                new AnnualNominalRateConversionStrategy();

        BigDecimal result =
                strategy.convertToMonthlyRate(
                        new BigDecimal("12.00")
                );

        assertEquals(
                new BigDecimal("0.0100000000000000"),
                result
        );
    }

    @Test
    void shouldConvertEffectiveAnnualRateToEquivalentMonthlyRate() {
        AnnualEffectiveRateConversionStrategy strategy =
                new AnnualEffectiveRateConversionStrategy();

        BigDecimal result =
                strategy.convertToMonthlyRate(
                        new BigDecimal("12.00")
                );

        assertEquals(
                new BigDecimal("0.0094887929345830"),
                result
        );
    }

    @Test
    void shouldConvertZeroEffectiveRateToZero() {
        AnnualEffectiveRateConversionStrategy strategy =
                new AnnualEffectiveRateConversionStrategy();

        BigDecimal result =
                strategy.convertToMonthlyRate(
                        BigDecimal.ZERO
                );

        assertEquals(
                new BigDecimal("0.0000000000000000"),
                result
        );
    }
}