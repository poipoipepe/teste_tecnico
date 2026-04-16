package com.inter.teste_tecnico.payload.request;

import jakarta.validation.constraints.NotBlank;

public record InvestorRequest(
        @NotBlank(message = "Name is required") String name
) {}
