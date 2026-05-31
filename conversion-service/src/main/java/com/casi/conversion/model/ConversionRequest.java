package com.casi.conversion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRequest {
    private double value;
    private Unit fromUnit;
    private Unit toUnit;
}
