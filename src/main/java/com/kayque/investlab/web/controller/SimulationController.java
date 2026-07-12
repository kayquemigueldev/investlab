package com.kayque.investlab.web.controller;

import com.kayque.investlab.application.dto.SimulationDetails;
import com.kayque.investlab.application.dto.SimulationHistoryItem;
import com.kayque.investlab.application.service.SimulationHistoryService;
import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;
import com.kayque.investlab.domain.model.SimulationRequest;
import com.kayque.investlab.domain.model.SimulationResult;
import com.kayque.investlab.domain.service.CompoundInterestSimulationService;
import com.kayque.investlab.web.form.SimulationForm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SimulationController {

    private final CompoundInterestSimulationService simulationService;
    private final SimulationHistoryService historyService;

    public SimulationController(
            CompoundInterestSimulationService simulationService,
            SimulationHistoryService historyService
    ) {
        this.simulationService = simulationService;
        this.historyService = historyService;
    }

    @GetMapping("/")
    public String showSimulator(
            @RequestParam(required = false) Long simulationId,
            @RequestParam(defaultValue = "false") boolean created,
            Model model
    ) {
        addOptions(model);
        model.addAttribute("created", created);

        if (simulationId == null) {
            model.addAttribute(
                    "simulationForm",
                    new SimulationForm()
            );

            return "index";
        }

        SimulationDetails details =
                historyService.findDetailsById(simulationId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Simulation not found"
                                )
                        );

        SimulationForm simulationForm =
                createFormFromHistory(details.history());

        model.addAttribute(
                "simulationForm",
                simulationForm
        );

        model.addAttribute(
                "result",
                details.recalculatedResult()
        );

        model.addAttribute(
                "comparison",
                details.scenarioComparison()
        );

        model.addAttribute(
                "savedSimulationId",
                details.history().id()
        );

        model.addAttribute("created", created);

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
            SimulationRequest request =
                    simulationForm.toDomain();

            SimulationResult result =
                    simulationService.simulate(request);

            Long savedSimulationId =
                    historyService.save(request, result);

            return "redirect:/?simulationId="
                    + savedSimulationId
                    + "&created=true";
        } catch (InvalidSimulationException exception) {
            model.addAttribute(
                    "simulationError",
                    exception.getMessage()
            );

            return "index";
        }
    }

    private SimulationForm createFormFromHistory(
            SimulationHistoryItem history
    ) {
        SimulationForm form = new SimulationForm();

        form.setInitialInvestment(
                history.initialInvestment()
        );

        form.setMonthlyContribution(
                history.monthlyContribution()
        );

        form.setInterestRatePercentage(
                history.interestRatePercentage()
        );

        form.setRatePeriod(
                history.ratePeriod()
        );

        form.setStartDate(
                history.startDate()
        );

        form.setEndDate(
                history.endDate()
        );

        form.setContributionTiming(
                history.contributionTiming()
        );

        return form;
    }

    private void addOptions(Model model) {
        model.addAttribute(
                "ratePeriods",
                RatePeriod.values()
        );

        model.addAttribute(
                "contributionTimings",
                ContributionTiming.values()
        );
    }
}