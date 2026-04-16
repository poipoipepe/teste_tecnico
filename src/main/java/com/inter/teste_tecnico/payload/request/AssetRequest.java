package com.inter.teste_tecnico.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AssetRequest(
        @NotBlank(message = "Ticker is required") @Size(max = 10, message = "Ticker must be at most 10 characters") String ticker,
        @NotBlank(message = "Company name is required") String companyName,
        @NotBlank(message = "Sector is required") String sector,
        @NotNull(message = "Current value is required") @DecimalMin(value = "0.01", message = "Current value must be greater than zero") BigDecimal currentValue
) {}
