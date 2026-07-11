package com.kayque.investlab.domain.strategy;

import com.kayque.investlab.domain.enums.RatePeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MonthlyRateConversionStrategy
        implements InterestRateConversionStrategy {

    private static final BigDecimal ONE_HUNDRED =
            new BigDecimal("100");

    private static final int RATE_SCALE = 16;

    @Override
    public boolean supports(RatePeriod ratePeriod) {
        return ratePeriod == RatePeriod.MONTHLY;
    }

    @Override
    public BigDecimal convertToMonthlyRate(
            BigDecimal interestRatePercentage
    ) {
        return interestRatePercentage.divide(
                ONE_HUNDRED,
                RATE_SCALE,
                RoundingMode.HALF_EVEN
        );
    }
}