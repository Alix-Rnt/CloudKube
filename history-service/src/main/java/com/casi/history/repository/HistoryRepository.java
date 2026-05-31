package com.casi.history.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.casi.history.model.ConversionResult;

@Repository
public class HistoryRepository {
    private final List<ConversionResult> conversions = new ArrayList<>();

    public ConversionResult save(ConversionResult conversion) {
        conversions.add(conversion);
        return conversion;
    }

    public List<ConversionResult> findAll() {
        return conversions;
    }

    public Optional<ConversionResult> findById(UUID id) {
        return conversions.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();        
    }

    public List<ConversionResult> findByTimestampAfter(LocalDateTime timestamp) {
        return conversions.stream().filter(c -> c.getTimestamp().isAfter(timestamp)).toList();
    }

    public void delete(UUID id) {
        conversions.removeIf(c -> c.getId().equals(id));
    }

    public void clear() {
        conversions.clear();
    }
}
