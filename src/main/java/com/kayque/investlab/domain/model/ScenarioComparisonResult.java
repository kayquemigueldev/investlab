package com.kayque.investlab.domain.model;

import java.util.List;

public record ScenarioComparisonResult(
        List<ScenarioProjection> scenarios,
        ScenarioProjection bestScenario
) {

    public ScenarioComparisonResult {
        scenarios = List.copyOf(scenarios);
    }
}