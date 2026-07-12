package com.kayque.investlab.domain.model;

import java.math.BigDecimal;

public record ScenarioProjection(
        String name,
        BigDecimal interestRatePercentage,
        SimulationResult simulationResult
) {
}