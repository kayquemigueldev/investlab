package com.kayque.investlab.domain.model;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;
import com.kayque.investlab.domain.validation.SimulationLimits;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record SimulationRequest(
        BigDecimal initialInvestment,
        BigDecimal monthlyContribution,
        BigDecimal interestRatePercentage,
        RatePeriod ratePeriod,
        LocalDate startDate,
        LocalDate endDate,
        ContributionTiming contributionTiming
) {

    public SimulationRequest {
        Objects.requireNonNull(
                initialInvestment,
                "Initial investment is required"
        );

        Objects.requireNonNull(
                monthlyContribution,
                "Monthly contribution is required"
        );

        Objects.requireNonNull(
                interestRatePercentage,
                "Interest rate is required"
        );

        Objects.requireNonNull(
                ratePeriod,
                "Rate period is required"
        );

        Objects.requireNonNull(
                startDate,
                "Start date is required"
        );

        Objects.requireNonNull(
                endDate,
                "End date is required"
        );

        Objects.requireNonNull(
                contributionTiming,
                "Contribution timing is required"
        );

        validateMonetaryValue(
                initialInvestment,
                "Initial investment"
        );

        validateMonetaryValue(
                monthlyContribution,
                "Monthly contribution"
        );

        if (initialInvestment.signum() == 0
                && monthlyContribution.signum() == 0) {
            throw new InvalidSimulationException(
                    "Initial investment and monthly contribution cannot both be zero"
            );
        }

        if (interestRatePercentage.signum() < 0) {
            throw new InvalidSimulationException(
                    "Interest rate cannot be negative"
            );
        }

        if (interestRatePercentage.compareTo(
                SimulationLimits.MAXIMUM_RATE_PERCENTAGE
        ) > 0) {
            throw new InvalidSimulationException(
                    "Interest rate cannot exceed 1000%"
            );
        }

        if (!endDate.isAfter(startDate)) {
            throw new InvalidSimulationException(
                    "End date must be after start date"
            );
        }

        long completeMonths =
                ChronoUnit.MONTHS.between(
                        startDate,
                        endDate
                );

        if (!startDate
                .plusMonths(completeMonths)
                .equals(endDate)) {
            throw new InvalidSimulationException(
                    "Simulation period must contain complete months"
            );
        }

        if (completeMonths
                > SimulationLimits.MAXIMUM_MONTHS) {
            throw new InvalidSimulationException(
                    "Simulation period cannot exceed 100 years"
            );
        }
    }

    public long numberOfMonths() {
        return ChronoUnit.MONTHS.between(
                startDate,
                endDate
        );
    }

    private static void validateMonetaryValue(
            BigDecimal value,
            String fieldName
    ) {
        if (value.signum() < 0) {
            throw new InvalidSimulationException(
                    fieldName + " cannot be negative"
            );
        }

        if (value.compareTo(
                SimulationLimits.MAXIMUM_MONETARY_AMOUNT
        ) > 0) {
            throw new InvalidSimulationException(
                    fieldName
                            + " exceeds the supported maximum"
            );
        }
    }
}