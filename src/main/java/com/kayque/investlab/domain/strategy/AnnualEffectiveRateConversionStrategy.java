package com.kayque.investlab.domain.strategy;

import com.kayque.investlab.domain.enums.RatePeriod;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AnnualEffectiveRateConversionStrategy
        implements InterestRateConversionStrategy {

    private static final MathContext CALCULATION_CONTEXT =
            new MathContext(34, RoundingMode.HALF_EVEN);

    private static final BigDecimal ONE_HUNDRED =
            new BigDecimal("100");

    private static final BigDecimal ONE = BigDecimal.ONE;

    private static final int MONTHS_PER_YEAR = 12;
    private static final int RATE_SCALE = 16;
    private static final int MAX_ITERATIONS = 100;

    private static final BigDecimal TOLERANCE =
            new BigDecimal("0.00000000000000000001");

    @Override
    public boolean supports(RatePeriod ratePeriod) {
        return ratePeriod == RatePeriod.ANNUAL_EFFECTIVE;
    }

    @Override
    public BigDecimal convertToMonthlyRate(
            BigDecimal interestRatePercentage
    ) {
        if (interestRatePercentage.signum() == 0) {
            return BigDecimal.ZERO.setScale(
                    RATE_SCALE,
                    RoundingMode.HALF_EVEN
            );
        }

        BigDecimal annualDecimalRate =
                interestRatePercentage.divide(
                        ONE_HUNDRED,
                        CALCULATION_CONTEXT
                );

        BigDecimal annualGrowthFactor =
                ONE.add(annualDecimalRate);

        BigDecimal monthlyGrowthFactor = nthRoot(
                annualGrowthFactor,
                MONTHS_PER_YEAR
        );

        return monthlyGrowthFactor
                .subtract(ONE)
                .setScale(
                        RATE_SCALE,
                        RoundingMode.HALF_EVEN
                );
    }

    private BigDecimal nthRoot(
            BigDecimal value,
            int root
    ) {
        BigDecimal rootAsDecimal =
                BigDecimal.valueOf(root);

        BigDecimal rootMinusOne =
                BigDecimal.valueOf(root - 1L);

        BigDecimal currentEstimate = ONE;

        for (int iteration = 0;
             iteration < MAX_ITERATIONS;
             iteration++) {

            BigDecimal previousEstimate =
                    currentEstimate;

            BigDecimal poweredEstimate =
                    currentEstimate.pow(
                            root - 1,
                            CALCULATION_CONTEXT
                    );

            BigDecimal division =
                    value.divide(
                            poweredEstimate,
                            CALCULATION_CONTEXT
                    );

            currentEstimate = rootMinusOne
                    .multiply(
                            currentEstimate,
                            CALCULATION_CONTEXT
                    )
                    .add(
                            division,
                            CALCULATION_CONTEXT
                    )
                    .divide(
                            rootAsDecimal,
                            CALCULATION_CONTEXT
                    );

            BigDecimal difference = currentEstimate
                    .subtract(previousEstimate)
                    .abs();

            if (difference.compareTo(TOLERANCE) < 0) {
                break;
            }
        }

        return currentEstimate;
    }
}