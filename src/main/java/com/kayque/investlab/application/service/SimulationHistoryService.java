package com.kayque.investlab.application.service;

import com.kayque.investlab.application.dto.SimulationHistoryItem;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.infrastructure.persistence.entity.SimulationEntity;
import com.kayque.investlab.infrastructure.persistence.mapper.SimulationPersistenceMapper;
import com.kayque.investlab.infrastructure.persistence.repository.SimulationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SimulationHistoryService {

    private final SimulationRepository repository;
    private final SimulationPersistenceMapper mapper;

    public SimulationHistoryService(
            SimulationRepository repository,
            SimulationPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
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
}