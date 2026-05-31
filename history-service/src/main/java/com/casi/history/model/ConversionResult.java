package com.casi.history.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResult {
    @Id
    private UUID id;

    @Column(name = "input_value")
    private double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_unit", length = 50)
    private Unit fromUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_unit", length = 50)
    private Unit toUnit;

    private double result;
    private LocalDateTime timestamp;
}
