package com.casi.history.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.casi.history.model.ConversionResult;
import com.casi.history.service.HistoryService;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversionResult save(@RequestBody ConversionResult result) {
        return historyService.saveHistory(result);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<ConversionResult> getAll() {
        return historyService.getAllHistory();
    }

    @DeleteMapping("/clear")
    public void clear() {
        historyService.clearHistory();
    }
}
