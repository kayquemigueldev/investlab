package com.kayque.investlab.application.dto;

import com.kayque.investlab.domain.model.ScenarioComparisonResult;
import com.kayque.investlab.domain.model.SimulationResult;

public record SimulationDetails(
        SimulationHistoryItem history,
        SimulationResult recalculatedResult,
        ScenarioComparisonResult scenarioComparison
) {
}