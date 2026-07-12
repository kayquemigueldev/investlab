package com.kayque.investlab.domain.model;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record GoalSimulationRequest(
        BigDecimal initialInvestment,
        BigDecimal monthlyContribution,
        BigDecimal targetAmount,
        BigDecimal interestRatePercentage,
        RatePeriod ratePeriod,
        LocalDate startDate,
        ContributionTiming contributionTiming
) {

    public GoalSimulationRequest {
        Objects.requireNonNull(
                initialInvestment,
                "Initial investment is required"
        );

        Objects.requireNonNull(
                monthlyContribution,
                "Monthly contribution is required"
        );

        Objects.requireNonNull(
                targetAmount,
                "Target amount is required"
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
                contributionTiming,
                "Contribution timing is required"
        );

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

        if (targetAmount.signum() <= 0) {
            throw new InvalidSimulationException(
                    "Target amount must be greater than zero"
            );
        }

        if (interestRatePercentage.signum() < 0) {
            throw new InvalidSimulationException(
                    "Interest rate cannot be negative"
            );
        }

        boolean targetNotReached =
                initialInvestment.compareTo(targetAmount) < 0;

        boolean noContribution =
                monthlyContribution.signum() == 0;

        boolean noProfitability =
                interestRatePercentage.signum() == 0;

        if (targetNotReached
                && noContribution
                && noProfitability) {

            throw new InvalidSimulationException(
                    "The goal cannot be reached without contributions or profitability"
            );
        }
    }
}