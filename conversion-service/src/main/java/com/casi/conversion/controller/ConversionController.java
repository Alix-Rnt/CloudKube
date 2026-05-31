package com.casi.conversion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.casi.conversion.model.ConversionRequest;
import com.casi.conversion.model.ConversionResult;
import com.casi.conversion.service.ConversionService;

@RestController
@RequestMapping("/api/conversion")
public class ConversionController {
    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversionResult createConversion(@RequestBody ConversionRequest request) {
        return conversionService.convert(request);
    }
}
