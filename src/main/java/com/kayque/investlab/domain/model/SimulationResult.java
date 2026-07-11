package com.kayque.investlab.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record SimulationResult(
        BigDecimal finalBalance,
        BigDecimal totalInvested,
        BigDecimal totalInterest,
        BigDecimal profitabilityPercentage,
        int numberOfMonths,
        List<MonthlyEvolution> monthlyEvolution
) {

    public SimulationResult {
        monthlyEvolution = List.copyOf(monthlyEvolution);
    }
}