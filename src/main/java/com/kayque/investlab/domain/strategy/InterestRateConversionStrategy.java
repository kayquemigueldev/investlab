package com.kayque.investlab.domain.strategy;

import com.kayque.investlab.domain.enums.RatePeriod;

import java.math.BigDecimal;

public interface InterestRateConversionStrategy {

    boolean supports(RatePeriod ratePeriod);

    BigDecimal convertToMonthlyRate(
            BigDecimal interestRatePercentage
    );
}