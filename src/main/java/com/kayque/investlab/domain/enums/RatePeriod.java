package com.kayque.investlab.domain.enums;

public enum RatePeriod {

    MONTHLY(12),
    ANNUAL(1);

    private final int periodsPerYear;

    RatePeriod(int periodsPerYear) {
        this.periodsPerYear = periodsPerYear;
    }

    public int getPeriodsPerYear() {
        return periodsPerYear;
    }
}