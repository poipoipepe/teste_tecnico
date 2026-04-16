package com.inter.teste_tecnico.controller;

import com.inter.teste_tecnico.payload.request.OrderRequest;
import com.inter.teste_tecnico.payload.response.OrderResponse;
import com.inter.teste_tecnico.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Received request to create order for investor: {}", request.investorId());
        OrderResponse createdOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByInvestor(@PathVariable Long investorId) {
        log.info("Received request to get orders for investor: {}", investorId);
        return ResponseEntity.ok(orderService.getOrdersByInvestor(investorId));
    }
}
