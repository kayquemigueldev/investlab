package com.kayque.investlab.web.controller;

import com.kayque.investlab.domain.enums.ContributionTiming;
import com.kayque.investlab.domain.enums.RatePeriod;
import com.kayque.investlab.domain.exception.InvalidSimulationException;
import com.kayque.investlab.domain.exception.UnreachableFinancialGoalException;
import com.kayque.investlab.domain.model.GoalSimulationResult;
import com.kayque.investlab.domain.service.FinancialGoalSimulationService;
import com.kayque.investlab.web.form.GoalSimulationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FinancialGoalController {

    private final FinancialGoalSimulationService goalService;

    public FinancialGoalController(
            FinancialGoalSimulationService goalService
    ) {
        this.goalService = goalService;
    }

    @GetMapping("/goal")
    public String showGoalSimulator(Model model) {
        model.addAttribute(
                "goalSimulationForm",
                new GoalSimulationForm()
        );

        addOptions(model);

        return "goal";
    }

    @PostMapping("/goal/calculate")
    public String calculateGoal(
            @Valid GoalSimulationForm goalSimulationForm,
            BindingResult bindingResult,
            Model model
    ) {
        addOptions(model);

        if (bindingResult.hasErrors()) {
            return "goal";
        }

        try {
            GoalSimulationResult goalResult =
                    goalService.simulate(
                            goalSimulationForm.toDomain()
                    );

            model.addAttribute("goalResult", goalResult);

            model.addAttribute(
                    "result",
                    goalResult
            );
        } catch (
                InvalidSimulationException
                | UnreachableFinancialGoalException exception
        ) {
            model.addAttribute(
                    "goalError",
                    exception.getMessage()
            );
        }

        return "goal";
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