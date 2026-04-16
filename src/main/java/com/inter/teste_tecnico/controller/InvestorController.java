package com.inter.teste_tecnico.controller;

import com.inter.teste_tecnico.payload.request.InvestorRequest;
import com.inter.teste_tecnico.payload.response.InvestorResponse;
import com.inter.teste_tecnico.payload.response.OrderResponse;
import com.inter.teste_tecnico.service.InvestorService;
import com.inter.teste_tecnico.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investors")
@RequiredArgsConstructor
@Slf4j
public class InvestorController {

    private final InvestorService investorService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<InvestorResponse> createInvestor(@Valid @RequestBody InvestorRequest request) {
        log.info("Received request to create investor: {}", request.name());
        InvestorResponse createdInvestor = investorService.createInvestor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvestor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestor(@PathVariable Long id) {
        log.info("Received request to delete investor with id: {}", id);
        investorService.deleteInvestor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getInvestorOrders(@PathVariable Long id) {
        log.info("Received request to get orders for investor with id: {}", id);
        return ResponseEntity.ok(orderService.getOrdersByInvestor(id));
    }
}
