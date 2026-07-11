package com.kayque.investlab.web.controller;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.domain.service.CompoundInterestSimulationService;
import com.kayque.investlab.web.form.SimulationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SimulationController {

    private final CompoundInterestSimulationService simulationService;

    public SimulationController(
            CompoundInterestSimulationService simulationService
    ) {
        this.simulationService = simulationService;
    }

    @GetMapping("/")
    public String showSimulator(Model model) {
        model.addAttribute("simulationForm", new SimulationForm());
        addOptions(model);

        return "index";
    }

    @PostMapping("/simulate")
    public String simulate(
            @Valid SimulationForm simulationForm,
            BindingResult bindingResult,
            Model model
    ) {
        addOptions(model);

        if (bindingResult.hasErrors()) {
            return "index";
        }

        try {
            SimulationResult result =
                    simulationService.simulate(simulationForm.toDomain());

            model.addAttribute("result", result);
        } catch (InvalidSimulationException exception) {
            model.addAttribute("simulationError", exception.getMessage());
        }

        return "index";
    }

    private void addOptions(Model model) {
        model.addAttribute("ratePeriods", RatePeriod.values());
        model.addAttribute(
                "contributionTimings",
                ContributionTiming.values()
        );
    }
}