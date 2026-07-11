package com.kayque.investlab.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyEvolution(
        int monthNumber,
        LocalDate referenceDate,
        BigDecimal openingBalance,
        BigDecimal contribution,
        BigDecimal interestEarned,
        BigDecimal closingBalance,
        BigDecimal totalInvested,
        BigDecimal totalInterest
) {
}