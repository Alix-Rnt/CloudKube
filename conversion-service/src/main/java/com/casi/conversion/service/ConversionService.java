package com.casi.conversion.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.casi.conversion.model.ConversionRequest;
import com.casi.conversion.model.ConversionResult;
import com.casi.conversion.model.Unit;

@Service
public class ConversionService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${history.service.url}")
    private String historyServiceUrl;

    public ConversionResult convert(ConversionRequest request) {
        double result = calculateConversion(request.getValue(), request.getFromUnit(), request.getToUnit());

        ConversionResult conversionResult = new ConversionResult(
            UUID.randomUUID(),
            request.getValue(),
            request.getFromUnit(),
            request.getToUnit(),
            result,
            LocalDateTime.now()
        );

        restTemplate.postForObject(historyServiceUrl + "/api/history", conversionResult, ConversionResult.class);

        return conversionResult;
    }

    private double calculateConversion(double value, Unit from, Unit to) {
        if (from == to) return value;

        switch (from) {
            // Temperature
            case CELSIUS -> {
                if (to == Unit.FAHRENHEIT) return (value * 9.0 / 5.0) + 32;
                if (to == Unit.KELVIN) return value + 273.15;
                break;
            }
            case FAHRENHEIT -> {
                if (to == Unit.CELSIUS) return (value - 32) * 5.0 / 9.0;
                if (to == Unit.KELVIN) return (value - 32) * 5.0 / 9.0 + 273.15;
                break;
            }
            case KELVIN -> {
                if (to == Unit.CELSIUS) return value - 273.15;
                if (to == Unit.FAHRENHEIT) return (value - 273.15) * 9.0 / 5.0 + 32;
                break;
            }
            
            // Distancs
            case KILOMETERS -> {
                if (to == Unit.MILES) return value * 0.621371;
                if (to == Unit.METERS) return value * 1000;
                break;
            }
            case MILES -> {
                if (to == Unit.KILOMETERS) return value / 0.621371;
                if (to == Unit.METERS) return value * 1609.34;
                break;
            }
            case METERS -> {
                if (to == Unit.KILOMETERS) return value / 1000;
                if (to == Unit.MILES) return value / 1609.34;
                break;
            }

            // Mass
            case KILOGRAMS -> {
                if (to == Unit.POUNDS) return value * 2.20462;
                if (to == Unit.GRAMS) return value * 1000;
                break;
            }
            case POUNDS -> {
                if (to == Unit.KILOGRAMS) return value / 2.20462;
                if (to == Unit.GRAMS) return value * 453.592;
                break;
            }
            case GRAMS -> {
                if (to == Unit.KILOGRAMS) return value / 1000;
                if (to == Unit.POUNDS) return value / 453.592;
                break;
            }
        }

        throw new IllegalArgumentException("Conversion not handled: " + from + " → " + to);
    }
}
