package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.payload.response.AssetPositionResponse;
import com.inter.teste_tecnico.payload.response.TradedResponse;
import com.inter.teste_tecnico.repository.InvestorRepository;
import com.inter.teste_tecnico.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final OrderRepository orderRepository;
    private final InvestorRepository investorRepository;

    @Transactional(readOnly = true)
    public List<AssetPositionResponse> getCurrentInvestorPosition(Long investorId) {
        log.info("Calculating current position for investor ID: {}", investorId);
        if (!investorRepository.existsById(investorId)) {
            log.error("Investor not found with ID: {}", investorId);
            throw new IllegalArgumentException("Investor not found with id: " + investorId);
        }
        List<AssetPositionResponse> positions = orderRepository.findPositionByInvestorId(investorId);
        log.info("Found {} asset positions for investor ID: {}", positions.size(), investorId);
        return positions;
    }

    @Transactional(readOnly = true)
    public List<TradedResponse> getMostTradedAssets(LocalDateTime from, LocalDateTime to) {
        log.info("Fetching most traded assets from {} to {}", from, to);
        List<TradedResponse> assets = orderRepository.findMostTradedAssets(from, to);
        log.info("Found {} most traded assets", assets.size());
        return assets;
    }
}
