package com.inter.teste_tecnico.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AssetCurrentValueRequest(
        @NotNull(message = "Current value is required")
        @DecimalMin(value = "0.01", message = "Current value must be greater than zero")
        @JsonProperty("current_value") BigDecimal currentValue
) {}
