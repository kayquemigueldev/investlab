package com.kayque.investlab.domain.strategy;

import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;

import java.util.List;

public class InterestRateStrategyResolver {

    private final List<InterestRateConversionStrategy> strategies;

    public InterestRateStrategyResolver(
            List<InterestRateConversionStrategy> strategies
    ) {
        this.strategies = List.copyOf(strategies);
    }

    public InterestRateConversionStrategy resolve(
            RatePeriod ratePeriod
    ) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(ratePeriod))
                .findFirst()
                .orElseThrow(() -> new InvalidSimulationException(
                        "Unsupported rate period: " + ratePeriod
                ));
    }
}