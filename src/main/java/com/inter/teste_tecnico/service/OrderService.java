package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.model.Asset;
import com.inter.teste_tecnico.model.Investor;
import com.inter.teste_tecnico.model.Order;
import com.inter.teste_tecnico.payload.request.OrderRequest;
import com.inter.teste_tecnico.payload.response.OrderResponse;
import com.inter.teste_tecnico.repository.AssetRepository;
import com.inter.teste_tecnico.repository.InvestorRepository;
import com.inter.teste_tecnico.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InvestorRepository investorRepository;
    private final AssetRepository assetRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Processing order creation for investor ID: {} and asset ID: {}", request.investorId(), request.assetId());

        Investor investor = investorRepository.findById(request.investorId())
                .orElseThrow(() -> {
                    log.error("Investor not found with ID: {}", request.investorId());
                    return new IllegalArgumentException("Investor not found with id: " + request.investorId());
                });

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> {
                    log.error("Asset not found with ID: {}", request.assetId());
                    return new IllegalArgumentException("Asset not found with id: " + request.assetId());
                });

        Order order = Order.builder()
                .investor(investor)
                .asset(asset)
                .quantity(request.quantity())
                .price(request.price())
                .type(request.type())
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        return toResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersByInvestor(Long investorId) {
        log.info("Fetching orders for investor ID: {}", investorId);
        List<OrderResponse> orders = orderRepository.findByInvestorId(investorId).stream()
                .map(this::toResponse)
                .toList();
        log.info("Found {} orders for investor ID: {}", orders.size(), investorId);
        return orders;
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getInvestor().getName(),
                order.getAsset().getTicker(),
                order.getQuantity(),
                order.getPrice(),
                order.getType(),
                order.getTimestamp()
        );
    }
}
