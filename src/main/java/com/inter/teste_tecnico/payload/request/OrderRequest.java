package com.inter.teste_tecnico.payload.request;

import com.inter.teste_tecnico.model.OrderType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull(message = "Investor ID is required") Long investorId,
        @NotNull(message = "Asset ID is required") Long assetId,
        @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity,
        @NotNull(message = "Price is required") @DecimalMin(value = "0.01", message = "Price must be greater than zero") BigDecimal price,
        @NotNull(message = "Order type is required (BUY or SELL)") OrderType type
) {}
