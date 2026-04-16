package com.inter.teste_tecnico.payload.response;

import java.math.BigDecimal;

public record AssetPositionResponse(
        String ticker,
        Long quantity,
        BigDecimal averageBuyPrice,
        BigDecimal currentValue,
        BigDecimal investedValue,
        BigDecimal marketValue,
        BigDecimal profitOrLoss,
        BigDecimal profitOrLossPercent
) {}
