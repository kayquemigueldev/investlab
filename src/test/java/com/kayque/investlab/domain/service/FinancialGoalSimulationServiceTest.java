package com.kayque.investlab.domain.service;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;
import com.kayque.investlab.domain.model.GoalSimulationRequest;
import com.kayque.investlab.domain.model.GoalSimulationResult;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialGoalSimulationServiceTest {

    private FinancialGoalSimulationService service;

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
                new FinancialGoalSimulationService(
                        resolver
                );
    }

    @Test
    void shouldCalculateWhenFinancialGoalWillBeReached() {
        GoalSimulationRequest request =
                new GoalSimulationRequest(
                        new BigDecimal("5000.00"),
                        new BigDecimal("500.00"),
                        new BigDecimal("100000.00"),
                        new BigDecimal("10.00"),
                        RatePeriod.ANNUAL_EFFECTIVE,
                        LocalDate.of(2026, 7, 12),
                        ContributionTiming.END_OF_PERIOD
                );

        GoalSimulationResult result =
                service.simulate(request);

        assertAll(
                () -> assertEquals(
                        111,
                        result.numberOfMonths()
                ),
                () -> assertEquals(
                        LocalDate.of(2035, 10, 12),
                        result.targetDate()
                ),
                () -> assertEquals(
                        new BigDecimal("100786.23"),
                        result.finalBalance()
                ),
                () -> assertEquals(
                        new BigDecimal("60500.00"),
                        result.totalInvested()
                ),
                () -> assertEquals(
                        new BigDecimal("40286.23"),
                        result.totalInterest()
                ),
                () -> assertEquals(
                        111,
                        result.monthlyEvolution().size()
                )
        );
    }

    @Test
    void shouldReturnImmediatelyWhenGoalIsAlreadyReached() {
        GoalSimulationRequest request =
                new GoalSimulationRequest(
                        new BigDecimal("100000.00"),
                        new BigDecimal("500.00"),
                        new BigDecimal("100000.00"),
                        new BigDecimal("10.00"),
                        RatePeriod.ANNUAL_EFFECTIVE,
                        LocalDate.of(2026, 7, 12),
                        ContributionTiming.END_OF_PERIOD
                );

        GoalSimulationResult result =
                service.simulate(request);

        assertAll(
                () -> assertEquals(
                        0,
                        result.numberOfMonths()
                ),
                () -> assertEquals(
                        LocalDate.of(2026, 7, 12),
                        result.targetDate()
                ),
                () -> assertEquals(
                        new BigDecimal("100000.00"),
                        result.finalBalance()
                ),
                () -> assertEquals(
                        0,
                        result.monthlyEvolution().size()
                )
        );
    }

    @Test
    void shouldRejectUnreachableGoalWithoutGrowth() {
        assertThrows(
                InvalidSimulationException.class,
                () -> new GoalSimulationRequest(
                        new BigDecimal("1000.00"),
                        BigDecimal.ZERO,
                        new BigDecimal("100000.00"),
                        BigDecimal.ZERO,
                        RatePeriod.MONTHLY,
                        LocalDate.of(2026, 7, 12),
                        ContributionTiming.END_OF_PERIOD
                )
        );
    }
}