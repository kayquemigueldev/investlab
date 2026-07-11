package com.kayque.investlab.config;

import com.kayque.investlab.domain.service.CompoundInterestSimulationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public CompoundInterestSimulationService compoundInterestSimulationService() {
        return new CompoundInterestSimulationService();
    }
}