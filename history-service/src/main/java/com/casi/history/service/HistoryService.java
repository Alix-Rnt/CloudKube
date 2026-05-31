package com.casi.history.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.casi.history.model.ConversionResult;
import com.casi.history.repository.HistoryRepository;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public ConversionResult saveHistory(ConversionResult result) {
        return historyRepository.save(result);
    }

    public List<ConversionResult> getAllHistory() {
        return historyRepository.findAll();
    }

    public void clearHistory() {
        historyRepository.clear();
    }
}
