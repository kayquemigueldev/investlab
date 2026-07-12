package com.kayque.investlab.infrastructure.persistence.mapper;

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
}