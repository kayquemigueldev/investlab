package com.kayque.investlab.domain.service;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.exception.UnreachableFinancialGoalException;
import com.kayque.investlab.domain.model.GoalSimulationRequest;
import com.kayque.investlab.domain.model.GoalSimulationResult;
import com.kayque.investlab.domain.model.MonthlyEvolution;
import com.kayque.investlab.domain.strategy.InterestRateConversionStrategy;
import com.kayque.investlab.domain.strategy.InterestRateStrategyResolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancialGoalSimulationService {

    private static final MathContext CALCULATION_CONTEXT =
            new MathContext(20, RoundingMode.HALF_EVEN);

    private static final int MONEY_SCALE = 2;
    private static final int MAXIMUM_MONTHS = 1200;

    private final InterestRateStrategyResolver rateStrategyResolver;

    public FinancialGoalSimulationService(
            InterestRateStrategyResolver rateStrategyResolver
    ) {
        this.rateStrategyResolver = rateStrategyResolver;
    }

    public GoalSimulationResult simulate(
            GoalSimulationRequest request
    ) {
        InterestRateConversionStrategy rateStrategy =
                rateStrategyResolver.resolve(
                        request.ratePeriod()
                );

        BigDecimal monthlyRate =
                rateStrategy.convertToMonthlyRate(
                        request.interestRatePercentage()
                );

        BigDecimal balance =
                money(request.initialInvestment());

        BigDecimal totalInvested =
                money(request.initialInvestment());

        BigDecimal totalInterest =
                money(BigDecimal.ZERO);

        List<MonthlyEvolution> evolution =
                new ArrayList<>();

        int month = 0;

        while (balance.compareTo(request.targetAmount()) < 0
                && month < MAXIMUM_MONTHS) {

            month++;

            BigDecimal openingBalance = balance;

            BigDecimal contribution =
                    money(request.monthlyContribution());

            if (request.contributionTiming()
                    == ContributionTiming.BEGINNING_OF_PERIOD) {

                balance = money(
                        balance.add(contribution)
                );

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

            balance = money(
                    balance.add(interestEarned)
            );

            totalInterest = money(
                    totalInterest.add(interestEarned)
            );

            if (request.contributionTiming()
                    == ContributionTiming.END_OF_PERIOD) {

                balance = money(
                        balance.add(contribution)
                );

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

        if (balance.compareTo(request.targetAmount()) < 0) {
            throw new UnreachableFinancialGoalException(
                    "The financial goal was not reached within 100 years"
            );
        }

        LocalDate targetDate =
                request.startDate().plusMonths(month);

        return new GoalSimulationResult(
                money(request.targetAmount()),
                balance,
                totalInvested,
                totalInterest,
                month,
                targetDate,
                evolution
        );
    }

    private BigDecimal money(BigDecimal value) {
        return value.setScale(
                MONEY_SCALE,
                RoundingMode.HALF_EVEN
        );
    }
}