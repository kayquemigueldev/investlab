package com.kayque.investlab.web.form;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.model.GoalSimulationRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.DecimalMax;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GoalSimulationForm {

    @NotNull(message = "Informe o investimento inicial")
    @DecimalMin(
            value = "0.00",
            message = "O investimento não pode ser negativo"
    )

    @DecimalMax(
            value = "99999999999999999.99",
            message = "O investimento excede o limite suportado"
    )

    private BigDecimal initialInvestment =
            new BigDecimal("5000.00");

    @NotNull(message = "Informe o aporte mensal")
    @DecimalMin(
            value = "0.00",
            message = "O aporte não pode ser negativo"
    )

    @DecimalMax(
            value = "99999999999999999.99",
            message = "O aporte excede o limite suportado"
    )

    private BigDecimal monthlyContribution =
            new BigDecimal("500.00");

    @NotNull(message = "Informe a meta financeira")
    @DecimalMin(
            value = "0.01",
            message = "A meta deve ser maior que zero"
    )

    @DecimalMax(
            value = "99999999999999999.99",
            message = "A meta excede o limite suportado"
    )

    private BigDecimal targetAmount =
            new BigDecimal("100000.00");

    @NotNull(message = "Informe a taxa de juros")
    @DecimalMin(
            value = "0.00",
            message = "A taxa não pode ser negativa"
    )

    @DecimalMax(
            value = "1000.00",
            message = "A taxa não pode ultrapassar 1000%"
    )

    private BigDecimal interestRatePercentage =
            new BigDecimal("10.00");

    @NotNull(message = "Selecione a periodicidade")
    private RatePeriod ratePeriod =
            RatePeriod.ANNUAL_EFFECTIVE;

    @NotNull(message = "Informe a data inicial")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate = LocalDate.now();

    @NotNull(message = "Selecione o momento do aporte")
    private ContributionTiming contributionTiming =
            ContributionTiming.END_OF_PERIOD;

    public GoalSimulationRequest toDomain() {
        return new GoalSimulationRequest(
                initialInvestment,
                monthlyContribution,
                targetAmount,
                interestRatePercentage,
                ratePeriod,
                startDate,
                contributionTiming
        );
    }

    public BigDecimal getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(
            BigDecimal initialInvestment
    ) {
        this.initialInvestment = initialInvestment;
    }

    public BigDecimal getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(
            BigDecimal monthlyContribution
    ) {
        this.monthlyContribution = monthlyContribution;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getInterestRatePercentage() {
        return interestRatePercentage;
    }

    public void setInterestRatePercentage(
            BigDecimal interestRatePercentage
    ) {
        this.interestRatePercentage =
                interestRatePercentage;
    }

    public RatePeriod getRatePeriod() {
        return ratePeriod;
    }

    public void setRatePeriod(RatePeriod ratePeriod) {
        this.ratePeriod = ratePeriod;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public ContributionTiming getContributionTiming() {
        return contributionTiming;
    }

    public void setContributionTiming(
            ContributionTiming contributionTiming
    ) {
        this.contributionTiming = contributionTiming;
    }
}