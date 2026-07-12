package com.kayque.investlab.infrastructure.persistence.entity;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulations")
public class SimulationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "initial_investment",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal initialInvestment;

    @Column(
            name = "monthly_contribution",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal monthlyContribution;

    @Column(
            name = "interest_rate_percentage",
            nullable = false,
            precision = 10,
            scale = 4
    )
    private BigDecimal interestRatePercentage;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "rate_period",
            nullable = false,
            length = 30
    )
    private RatePeriod ratePeriod;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "contribution_timing",
            nullable = false,
            length = 30
    )
    private ContributionTiming contributionTiming;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "number_of_months", nullable = false)
    private int numberOfMonths;

    @Column(
            name = "final_balance",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal finalBalance;

    @Column(
            name = "total_invested",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal totalInvested;

    @Column(
            name = "total_interest",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal totalInterest;

    @Column(
            name = "profitability_percentage",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal profitabilityPercentage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected SimulationEntity() {
    }

    public SimulationEntity(
            BigDecimal initialInvestment,
            BigDecimal monthlyContribution,
            BigDecimal interestRatePercentage,
            RatePeriod ratePeriod,
            ContributionTiming contributionTiming,
            LocalDate startDate,
            LocalDate endDate,
            int numberOfMonths,
            BigDecimal finalBalance,
            BigDecimal totalInvested,
            BigDecimal totalInterest,
            BigDecimal profitabilityPercentage,
            LocalDateTime createdAt
    ) {
        this.initialInvestment = initialInvestment;
        this.monthlyContribution = monthlyContribution;
        this.interestRatePercentage = interestRatePercentage;
        this.ratePeriod = ratePeriod;
        this.contributionTiming = contributionTiming;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfMonths = numberOfMonths;
        this.finalBalance = finalBalance;
        this.totalInvested = totalInvested;
        this.totalInterest = totalInterest;
        this.profitabilityPercentage = profitabilityPercentage;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getInitialInvestment() {
        return initialInvestment;
    }

    public BigDecimal getMonthlyContribution() {
        return monthlyContribution;
    }

    public BigDecimal getInterestRatePercentage() {
        return interestRatePercentage;
    }

    public RatePeriod getRatePeriod() {
        return ratePeriod;
    }

    public ContributionTiming getContributionTiming() {
        return contributionTiming;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public BigDecimal getFinalBalance() {
        return finalBalance;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public BigDecimal getProfitabilityPercentage() {
        return profitabilityPercentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}