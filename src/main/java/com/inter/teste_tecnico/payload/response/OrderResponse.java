package com.inter.teste_tecnico.payload.response;

import com.inter.teste_tecnico.model.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String investorName,
        String assetTicker,
        Integer quantity,
        BigDecimal price,
        OrderType type,
        LocalDateTime timestamp
) {}
