package com.kayque.investlab.domain.validation;

import java.math.BigDecimal;

public final class SimulationLimits {

    public static final BigDecimal MAXIMUM_MONETARY_AMOUNT =
            new BigDecimal("99999999999999999.99");

    public static final BigDecimal MAXIMUM_RATE_PERCENTAGE =
            new BigDecimal("1000.00");

    public static final int MAXIMUM_MONTHS = 1200;

    private SimulationLimits() {
    }
}