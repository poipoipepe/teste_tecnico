package com.inter.teste_tecnico.payload.response;

import java.math.BigDecimal;

public record AssetResponse(Long id, String ticker, String companyName, String sector, BigDecimal currentValue) {}
