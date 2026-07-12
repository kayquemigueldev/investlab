package com.kayque.investlab.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record GoalSimulationResult(
        BigDecimal targetAmount,
        BigDecimal finalBalance,
        BigDecimal totalInvested,
        BigDecimal totalInterest,
        int numberOfMonths,
        LocalDate targetDate,
        List<MonthlyEvolution> monthlyEvolution
) {

    public GoalSimulationResult {
        monthlyEvolution = List.copyOf(monthlyEvolution);
    }
}