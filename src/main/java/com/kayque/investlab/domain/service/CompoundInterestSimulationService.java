package com.kayque.investlab.domain.service;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.model.MonthlyEvolution;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.domain.strategy.InterestRateConversionStrategy;
import com.kayque.investlab.domain.strategy.InterestRateStrategyResolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompoundInterestSimulationService {

    private static final MathContext CALCULATION_CONTEXT =
            new MathContext(20, RoundingMode.HALF_EVEN);

    private static final int MONEY_SCALE = 2;
    private static final int RATE_SCALE = 16;

    private static final BigDecimal ONE_HUNDRED =
            new BigDecimal("100");

    private final InterestRateStrategyResolver rateStrategyResolver;

    public CompoundInterestSimulationService(
            InterestRateStrategyResolver rateStrategyResolver
    ) {
        this.rateStrategyResolver = rateStrategyResolver;
    }

    public SimulationResult simulate(SimulationRequest request) {
        InterestRateConversionStrategy rateStrategy =
                rateStrategyResolver.resolve(request.ratePeriod());

        BigDecimal monthlyRate =
                rateStrategy.convertToMonthlyRate(
                        request.interestRatePercentage()
                );

        BigDecimal balance = money(request.initialInvestment());
        BigDecimal totalInvested = money(request.initialInvestment());
        BigDecimal totalInterest = money(BigDecimal.ZERO);

        List<MonthlyEvolution> evolution = new ArrayList<>();

        int numberOfMonths = Math.toIntExact(
                request.numberOfMonths()
        );

        for (int month = 1; month <= numberOfMonths; month++) {
            BigDecimal openingBalance = balance;
            BigDecimal contribution = money(
                    request.monthlyContribution()
            );

            if (request.contributionTiming()
                    == ContributionTiming.BEGINNING_OF_PERIOD) {

                balance = money(balance.add(contribution));
                totalInvested = money(
                        totalInvested.add(contribution)
                );
            }

            BigDecimal interestEarned = money(
                    balance.multiply(
                            monthlyRate,
                            CALCULATION_CONTEXT
                    )
            );

            balance = money(balance.add(interestEarned));
            totalInterest = money(
                    totalInterest.add(interestEarned)
            );

            if (request.contributionTiming()
                    == ContributionTiming.END_OF_PERIOD) {

                balance = money(balance.add(contribution));
                totalInvested = money(
                        totalInvested.add(contribution)
                );
            }

            LocalDate referenceDate =
                    request.startDate().plusMonths(month);

            evolution.add(new MonthlyEvolution(
                    month,
                    referenceDate,
                    openingBalance,
                    contribution,
                    interestEarned,
                    balance,
                    totalInvested,
                    totalInterest
            ));
        }

        BigDecimal profitabilityPercentage =
                calculateProfitability(
                        totalInterest,
                        totalInvested
                );

        return new SimulationResult(
                balance,
                totalInvested,
                totalInterest,
                profitabilityPercentage,
                numberOfMonths,
                evolution
        );
    }

    private BigDecimal calculateProfitability(
            BigDecimal totalInterest,
            BigDecimal totalInvested
    ) {
        if (totalInvested.signum() == 0) {
            return BigDecimal.ZERO.setScale(
                    MONEY_SCALE,
                    RoundingMode.HALF_EVEN
            );
        }

        return totalInterest
                .divide(
                        totalInvested,
                        RATE_SCALE,
                        RoundingMode.HALF_EVEN
                )
                .multiply(ONE_HUNDRED)
                .setScale(
                        MONEY_SCALE,
                        RoundingMode.HALF_EVEN
                );
    }

    private BigDecimal money(BigDecimal value) {
        return value.setScale(
                MONEY_SCALE,
                RoundingMode.HALF_EVEN
        );
    }
}