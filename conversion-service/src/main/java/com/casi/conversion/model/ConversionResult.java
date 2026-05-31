package com.casi.conversion.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResult {
    private UUID id;
    private double value;
    private Unit fromUnit;
    private Unit toUnit;
    private double result;
    private LocalDateTime timestamp;
}
