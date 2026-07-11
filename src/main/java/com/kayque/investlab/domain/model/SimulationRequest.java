package com.kayque.investlab.domain.model;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        Objects.requireNonNull(initialInvestment, "Initial investment is required");
        Objects.requireNonNull(monthlyContribution, "Monthly contribution is required");
        Objects.requireNonNull(interestRatePercentage, "Interest rate is required");
        Objects.requireNonNull(ratePeriod, "Rate period is required");
        Objects.requireNonNull(startDate, "Start date is required");
        Objects.requireNonNull(endDate, "End date is required");
        Objects.requireNonNull(contributionTiming, "Contribution timing is required");

        if (initialInvestment.signum() < 0) {
            throw new InvalidSimulationException(
                    "Initial investment cannot be negative"
            );
        }

        if (monthlyContribution.signum() < 0) {
            throw new InvalidSimulationException(
                    "Monthly contribution cannot be negative"
            );
        }

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

        if (!endDate.isAfter(startDate)) {
            throw new InvalidSimulationException(
                    "End date must be after start date"
            );
        }
    }
}