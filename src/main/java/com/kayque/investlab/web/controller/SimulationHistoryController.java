package com.kayque.investlab.web.controller;

import com.kayque.investlab.application.dto.SimulationHistoryItem;
import com.kayque.investlab.application.service.SimulationHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}