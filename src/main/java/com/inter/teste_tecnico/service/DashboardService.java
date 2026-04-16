package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.model.Order;
import com.inter.teste_tecnico.model.OrderType;
import com.inter.teste_tecnico.payload.response.AssetPositionResponse;
import com.inter.teste_tecnico.payload.response.TradedResponse;
import com.inter.teste_tecnico.repository.InvestorRepository;
import com.inter.teste_tecnico.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        List<Order> orders = orderRepository.findByInvestorIdOrderByTimestampAsc(investorId);
        Map<Long, PositionAccumulator> positionsByAsset = new LinkedHashMap<>();

        for (Order order : orders) {
            Long assetId = order.getAsset().getId();
            PositionAccumulator accumulator = positionsByAsset.computeIfAbsent(assetId, key ->
                    new PositionAccumulator(order.getAsset().getTicker(), order.getAsset().getCurrentValue()));
            accumulator.apply(order);
        }

        List<AssetPositionResponse> positions = positionsByAsset.values().stream()
                .filter(acc -> acc.quantity > 0)
                .map(PositionAccumulator::toResponse)
                .toList();

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

    private static class PositionAccumulator {
        private final String ticker;
        private final BigDecimal currentValue;
        private long quantity;
        private BigDecimal averageBuyPrice;

        private PositionAccumulator(String ticker, BigDecimal currentValue) {
            this.ticker = ticker;
            this.currentValue = currentValue != null ? currentValue : BigDecimal.ZERO;
            this.quantity = 0L;
            this.averageBuyPrice = BigDecimal.ZERO;
        }

        private void apply(Order order) {
            long orderQuantity = order.getQuantity();
            if (order.getType() == OrderType.BUY) {
                BigDecimal currentCost = averageBuyPrice.multiply(BigDecimal.valueOf(quantity));
                BigDecimal buyCost = order.getPrice().multiply(BigDecimal.valueOf(orderQuantity));
                quantity += orderQuantity;
                averageBuyPrice = currentCost.add(buyCost)
                        .divide(BigDecimal.valueOf(quantity), 4, RoundingMode.HALF_UP);
                return;
            }

            if (orderQuantity >= quantity) {
                quantity = 0L;
                averageBuyPrice = BigDecimal.ZERO;
                return;
            }

            quantity -= orderQuantity;
        }

        private AssetPositionResponse toResponse() {
            BigDecimal quantityDecimal = BigDecimal.valueOf(quantity);
            BigDecimal investedValue = averageBuyPrice.multiply(quantityDecimal).setScale(4, RoundingMode.HALF_UP);
            BigDecimal marketValue = currentValue.multiply(quantityDecimal).setScale(4, RoundingMode.HALF_UP);
            BigDecimal profitOrLoss = marketValue.subtract(investedValue).setScale(4, RoundingMode.HALF_UP);

            BigDecimal profitOrLossPercent = BigDecimal.ZERO;
            if (investedValue.compareTo(BigDecimal.ZERO) > 0) {
                profitOrLossPercent = profitOrLoss
                        .multiply(BigDecimal.valueOf(100))
                        .divide(investedValue, 2, RoundingMode.HALF_UP);
            }

            return new AssetPositionResponse(
                    ticker,
                    quantity,
                    averageBuyPrice,
                    currentValue,
                    investedValue,
                    marketValue,
                    profitOrLoss,
                    profitOrLossPercent);
        }
    }
}
