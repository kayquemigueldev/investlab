package com.kayque.investlab.web.controller;

import com.kayque.investlab.application.dto.SimulationDetails;
import com.kayque.investlab.application.dto.SimulationHistoryItem;
import com.kayque.investlab.application.service.SimulationHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class SimulationHistoryController {

    private final SimulationHistoryService historyService;

    public SimulationHistoryController(
            SimulationHistoryService historyService
    ) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<SimulationHistoryItem> simulations =
                historyService.findRecent();

        model.addAttribute("simulations", simulations);

        return "history";
    }

    @GetMapping("/history/{id}")
    public String showDetails(
            @PathVariable Long id,
            Model model
    ) {
        SimulationDetails details =
                historyService.findDetailsById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Simulation not found"
                                )
                        );

        model.addAttribute("details", details);
        model.addAttribute(
                "result",
                details.recalculatedResult()
        );

        return "simulation-details";
    }
}