package com.kayque.investlab.config;

import com.kayque.investlab.domain.service.CompoundInterestSimulationService;
import com.kayque.investlab.domain.strategy.AnnualNominalRateConversionStrategy;
import com.kayque.investlab.domain.strategy.InterestRateConversionStrategy;
import com.kayque.investlab.domain.strategy.InterestRateStrategyResolver;
import com.kayque.investlab.domain.strategy.MonthlyRateConversionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainConfiguration {

    @Bean
    public MonthlyRateConversionStrategy monthlyRateConversionStrategy() {
        return new MonthlyRateConversionStrategy();
    }

    @Bean
    public AnnualNominalRateConversionStrategy
    annualNominalRateConversionStrategy() {
        return new AnnualNominalRateConversionStrategy();
    }

    @Bean
    public InterestRateStrategyResolver interestRateStrategyResolver(
            List<InterestRateConversionStrategy> strategies
    ) {
        return new InterestRateStrategyResolver(strategies);
    }

    @Bean
    public CompoundInterestSimulationService
    compoundInterestSimulationService(
            InterestRateStrategyResolver rateStrategyResolver
    ) {
        return new CompoundInterestSimulationService(
                rateStrategyResolver
        );
    }
}