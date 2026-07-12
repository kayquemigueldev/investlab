package com.kayque.investlab.domain.service;

import com.kayque.investlab.domain.exception.InvalidSimulationException;
import com.kayque.investlab.domain.model.ScenarioComparisonResult;
import com.kayque.investlab.domain.model.ScenarioProjection;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class ScenarioComparisonService {

    private static final int RATE_SCALE = 4;

    private static final List<ScenarioDefinition>
            SCENARIO_DEFINITIONS = List.of(
            new ScenarioDefinition(
                    "Conservador",
                    new BigDecimal("0.80")
            ),
            new ScenarioDefinition(
                    "Base",
                    BigDecimal.ONE
            ),
            new ScenarioDefinition(
                    "Otimista",
                    new BigDecimal("1.20")
            )
    );

    private final CompoundInterestSimulationService simulationService;

    public ScenarioComparisonService(
            CompoundInterestSimulationService simulationService
    ) {
        this.simulationService = simulationService;
    }

    public ScenarioComparisonResult compare(
            SimulationRequest baseRequest
    ) {
        List<ScenarioProjection> scenarios =
                SCENARIO_DEFINITIONS.stream()
                        .map(definition -> createProjection(
                                baseRequest,
                                definition
                        ))
                        .sorted(
                                Comparator.comparing(
                                        this::finalBalance
                                ).reversed()
                        )
                        .toList();

        ScenarioProjection bestScenario = scenarios.stream()
                .max(Comparator.comparing(this::finalBalance))
                .orElseThrow(() -> new InvalidSimulationException(
                        "No comparison scenarios were generated"
                ));

        return new ScenarioComparisonResult(
                scenarios,
                bestScenario
        );
    }

    private ScenarioProjection createProjection(
            SimulationRequest baseRequest,
            ScenarioDefinition definition
    ) {
        BigDecimal adjustedRate =
                baseRequest.interestRatePercentage()
                        .multiply(definition.rateMultiplier())
                        .setScale(
                                RATE_SCALE,
                                RoundingMode.HALF_EVEN
                        );

        SimulationRequest adjustedRequest =
                new SimulationRequest(
                        baseRequest.initialInvestment(),
                        baseRequest.monthlyContribution(),
                        adjustedRate,
                        baseRequest.ratePeriod(),
                        baseRequest.startDate(),
                        baseRequest.endDate(),
                        baseRequest.contributionTiming()
                );

        SimulationResult result =
                simulationService.simulate(adjustedRequest);

        return new ScenarioProjection(
                definition.name(),
                adjustedRate,
                result
        );
    }

    private BigDecimal finalBalance(
            ScenarioProjection projection
    ) {
        return projection
                .simulationResult()
                .finalBalance();
    }

    private record ScenarioDefinition(
            String name,
            BigDecimal rateMultiplier
    ) {
    }
}