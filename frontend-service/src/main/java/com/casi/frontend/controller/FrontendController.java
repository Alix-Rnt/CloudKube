package com.casi.frontend.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.casi.frontend.model.ConversionRequest;
import com.casi.frontend.model.Unit;

@Controller
public class FrontendController {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${conversion.service.url}")
    private String conversionServiceUrl;

    @Value("${history.service.url}")
    private String historyServiceUrl;

    @GetMapping("/")
    public String index(Model model) {
        Object[] history = restTemplate.getForObject(historyServiceUrl + "/api/history", Object[].class);
        model.addAttribute("history", history != null ? Arrays.asList(history) : List.of());
        model.addAttribute("units", List.of("CELSIUS", "FAHRENHEIT", "KELVIN",
                                            "KILOMETERS", "MILES", "METERS",
                                            "KILOGRAMS", "POUNDS", "GRAMS"));
        return "index";
    }

    @PostMapping("/convert")
    public String convert(@RequestParam double value,
                        @RequestParam Unit fromUnit,
                        @RequestParam Unit toUnit) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConversionRequest> entity = new HttpEntity<>(
            new ConversionRequest(value, fromUnit, toUnit), 
            headers
        );

        restTemplate.postForObject(
            conversionServiceUrl + "/api/conversion",
            entity,
            Object.class
        );

        return "redirect:/";
    }

    @PostMapping("/clear")
    public String clear() {
        restTemplate.postForObject(
            historyServiceUrl + "/api/history/clear",
            null,
            Object.class
        );
        return "redirect:/";
    }
}