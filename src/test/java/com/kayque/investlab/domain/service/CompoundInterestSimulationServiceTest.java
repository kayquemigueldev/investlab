package com.kayque.investlab.domain.service;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.domain.strategy.AnnualEffectiveRateConversionStrategy;
import com.kayque.investlab.domain.strategy.AnnualNominalRateConversionStrategy;
import com.kayque.investlab.domain.strategy.InterestRateStrategyResolver;
import com.kayque.investlab.domain.strategy.MonthlyRateConversionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompoundInterestSimulationServiceTest {

    private CompoundInterestSimulationService service;

    @BeforeEach
    void setUp() {
        InterestRateStrategyResolver resolver =
                new InterestRateStrategyResolver(
                        List.of(
                                new MonthlyRateConversionStrategy(),
                                new AnnualNominalRateConversionStrategy(),
                                new AnnualEffectiveRateConversionStrategy()
                        )
                );

        service =
                new CompoundInterestSimulationService(
                        resolver
                );
    }

    @Test
    void shouldSimulateTwelveMonthsWithEndOfPeriodContributions() {
        SimulationRequest request = createRequest(
                RatePeriod.MONTHLY,
                new BigDecimal("1.00"),
                ContributionTiming.END_OF_PERIOD
        );

        SimulationResult result =
                service.simulate(request);

        assertAll(
                () -> assertEquals(
                        new BigDecimal("3663.32"),
                        result.finalBalance()
                ),
                () -> assertEquals(
                        new BigDecimal("3400.00"),
                        result.totalInvested()
                ),
                () -> assertEquals(
                        new BigDecimal("263.32"),
                        result.totalInterest()
                ),
                () -> assertEquals(
                        new BigDecimal("7.74"),
                        result.profitabilityPercentage()
                ),
                () -> assertEquals(
                        12,
                        result.numberOfMonths()
                ),
                () -> assertEquals(
                        12,
                        result.monthlyEvolution().size()
                )
        );
    }

    @Test
    void shouldSimulateTwelveMonthsWithBeginningContributions() {
        SimulationRequest request = createRequest(
                RatePeriod.MONTHLY,
                new BigDecimal("1.00"),
                ContributionTiming.BEGINNING_OF_PERIOD
        );

        SimulationResult result =
                service.simulate(request);

        assertAll(
                () -> assertEquals(
                        new BigDecimal("3688.68"),
                        result.finalBalance()
                ),
                () -> assertEquals(
                        new BigDecimal("3400.00"),
                        result.totalInvested()
                ),
                () -> assertEquals(
                        new BigDecimal("288.68"),
                        result.totalInterest()
                ),
                () -> assertEquals(
                        new BigDecimal("8.49"),
                        result.profitabilityPercentage()
                )
        );
    }

    @Test
    void shouldProduceSameResultForEquivalentNominalRates() {
        SimulationResult monthlyResult =
                service.simulate(
                        createRequest(
                                RatePeriod.MONTHLY,
                                new BigDecimal("1.00"),
                                ContributionTiming.END_OF_PERIOD
                        )
                );

        SimulationResult annualResult =
                service.simulate(
                        createRequest(
                                RatePeriod.ANNUAL,
                                new BigDecimal("12.00"),
                                ContributionTiming.END_OF_PERIOD
                        )
                );

        assertEquals(
                monthlyResult.finalBalance(),
                annualResult.finalBalance()
        );
    }

    @Test
    void shouldKeepMonthlyEvolutionImmutable() {
        SimulationResult result =
                service.simulate(
                        createRequest(
                                RatePeriod.MONTHLY,
                                new BigDecimal("1.00"),
                                ContributionTiming.END_OF_PERIOD
                        )
                );

        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> result.monthlyEvolution().clear()
        );
    }

    private SimulationRequest createRequest(
            RatePeriod ratePeriod,
            BigDecimal rate,
            ContributionTiming contributionTiming
    ) {
        return new SimulationRequest(
                new BigDecimal("1000.00"),
                new BigDecimal("200.00"),
                rate,
                ratePeriod,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2027, 7, 12),
                contributionTiming
        );
    }
}