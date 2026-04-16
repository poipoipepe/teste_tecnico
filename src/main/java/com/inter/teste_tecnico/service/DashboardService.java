package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.payload.response.AssetTradeCountResponse;
import com.inter.teste_tecnico.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final OrderRepository orderRepository;

    public BigDecimal getCurrentPosition() {
        log.info("Calculating current position (sum of all BUY orders)");
        BigDecimal position = orderRepository.calculateCurrentPosition();
        BigDecimal result = position != null ? position : BigDecimal.ZERO;
        log.info("Calculated current position: {}", result);
        return result;
    }

    public List<AssetTradeCountResponse> getMostTradedAssets() {
        log.info("Fetching most traded assets");
        List<AssetTradeCountResponse> assets = orderRepository.findMostTradedAssets();
        log.info("Found {} most traded assets", assets.size());
        return assets;
    }
}
