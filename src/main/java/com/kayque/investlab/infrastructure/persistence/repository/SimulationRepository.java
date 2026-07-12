package com.kayque.investlab.infrastructure.persistence.repository;

import com.kayque.investlab.infrastructure.persistence.entity.SimulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepository
        extends JpaRepository<SimulationEntity, Long> {

    List<SimulationEntity> findTop20ByOrderByCreatedAtDesc();
}