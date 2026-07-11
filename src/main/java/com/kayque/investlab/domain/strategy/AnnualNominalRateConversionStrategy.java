package com.kayque.investlab.domain.strategy;

import com.kayque.investlab.domain.enums.RatePeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnnualNominalRateConversionStrategy
        implements InterestRateConversionStrategy {

    private static final BigDecimal ONE_HUNDRED =
            new BigDecimal("100");

    private static final BigDecimal MONTHS_PER_YEAR =
            new BigDecimal("12");

    private static final int RATE_SCALE = 16;

    @Override
    public boolean supports(RatePeriod ratePeriod) {
        return ratePeriod == RatePeriod.ANNUAL;
    }

    @Override
    public BigDecimal convertToMonthlyRate(
            BigDecimal interestRatePercentage
    ) {
        BigDecimal annualDecimalRate =
                interestRatePercentage.divide(
                        ONE_HUNDRED,
                        RATE_SCALE,
                        RoundingMode.HALF_EVEN
                );

        return annualDecimalRate.divide(
                MONTHS_PER_YEAR,
                RATE_SCALE,
                RoundingMode.HALF_EVEN
        );
    }
}