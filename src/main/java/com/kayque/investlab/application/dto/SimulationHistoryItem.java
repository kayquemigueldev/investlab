package com.kayque.investlab.application.dto;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SimulationHistoryItem(
        Long id,
        BigDecimal initialInvestment,
        BigDecimal monthlyContribution,
        BigDecimal interestRatePercentage,
        RatePeriod ratePeriod,
        ContributionTiming contributionTiming,
        LocalDate startDate,
        LocalDate endDate,
        int numberOfMonths,
        BigDecimal finalBalance,
        BigDecimal totalInvested,
        BigDecimal totalInterest,
        BigDecimal profitabilityPercentage,
        LocalDateTime createdAt
) {
}