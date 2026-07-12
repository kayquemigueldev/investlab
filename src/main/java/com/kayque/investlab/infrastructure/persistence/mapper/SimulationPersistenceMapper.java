package com.kayque.investlab.infrastructure.persistence.mapper;

import com.kayque.investlab.application.dto.SimulationHistoryItem;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.infrastructure.persistence.entity.SimulationEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SimulationPersistenceMapper {

    public SimulationEntity toEntity(
            SimulationRequest request,
            SimulationResult result
    ) {
        return new SimulationEntity(
                request.initialInvestment(),
                request.monthlyContribution(),
                request.interestRatePercentage(),
                request.ratePeriod(),
                request.contributionTiming(),
                request.startDate(),
                request.endDate(),
                result.numberOfMonths(),
                result.finalBalance(),
                result.totalInvested(),
                result.totalInterest(),
                result.profitabilityPercentage(),
                LocalDateTime.now()
        );
    }

    public SimulationHistoryItem toHistoryItem(
            SimulationEntity entity
    ) {
        return new SimulationHistoryItem(
                entity.getId(),
                entity.getInitialInvestment(),
                entity.getMonthlyContribution(),
                entity.getInterestRatePercentage(),
                entity.getRatePeriod(),
                entity.getContributionTiming(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNumberOfMonths(),
                entity.getFinalBalance(),
                entity.getTotalInvested(),
                entity.getTotalInterest(),
                entity.getProfitabilityPercentage(),
                entity.getCreatedAt()
        );
    }

    public SimulationRequest toDomainRequest(
            SimulationEntity entity
    ) {
        return new SimulationRequest(
                entity.getInitialInvestment(),
                entity.getMonthlyContribution(),
                entity.getInterestRatePercentage(),
                entity.getRatePeriod(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getContributionTiming()
        );
    }
}