package com.kayque.investlab.application.service;

import com.kayque.investlab.application.dto.SimulationDetails;
import com.kayque.investlab.application.dto.SimulationHistoryItem;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.domain.service.CompoundInterestSimulationService;
import com.kayque.investlab.infrastructure.persistence.entity.SimulationEntity;
import com.kayque.investlab.infrastructure.persistence.mapper.SimulationPersistenceMapper;
import com.kayque.investlab.infrastructure.persistence.repository.SimulationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SimulationHistoryService {

    private final SimulationRepository repository;
    private final SimulationPersistenceMapper mapper;
    private final CompoundInterestSimulationService simulationService;

    public SimulationHistoryService(
            SimulationRepository repository,
            SimulationPersistenceMapper mapper,
            CompoundInterestSimulationService simulationService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.simulationService = simulationService;
    }

    @Transactional
    public Long save(
            SimulationRequest request,
            SimulationResult result
    ) {
        SimulationEntity entity =
                mapper.toEntity(request, result);

        SimulationEntity savedEntity =
                repository.save(entity);

        return savedEntity.getId();
    }

    @Transactional(readOnly = true)
    public List<SimulationHistoryItem> findRecent() {
        return repository
                .findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toHistoryItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<SimulationDetails> findDetailsById(Long id) {
        return repository.findById(id)
                .map(this::createDetails);
    }

    private SimulationDetails createDetails(
            SimulationEntity entity
    ) {
        SimulationRequest request =
                mapper.toDomainRequest(entity);

        SimulationResult recalculatedResult =
                simulationService.simulate(request);

        SimulationHistoryItem historyItem =
                mapper.toHistoryItem(entity);

        return new SimulationDetails(
                historyItem,
                recalculatedResult
        );
    }
}