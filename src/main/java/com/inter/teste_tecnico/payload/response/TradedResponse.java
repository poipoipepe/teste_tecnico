package com.inter.teste_tecnico.payload.response;

public record TradedResponse(
        String ticker,
        Long buyCount,
        Long sellCount
) {}
