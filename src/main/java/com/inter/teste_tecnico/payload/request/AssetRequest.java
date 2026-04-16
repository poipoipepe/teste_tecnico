package com.inter.teste_tecnico.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AssetRequest(
        @NotBlank(message = "Ticker is required") @Size(max = 10, message = "Ticker must be at most 10 characters") String ticker,
        @NotBlank(message = "Company name is required") String companyName,
        @NotBlank(message = "Sector is required") String sector
) {}
