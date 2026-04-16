package com.inter.teste_tecnico.controller;

import com.inter.teste_tecnico.payload.response.AssetTradeCountResponse;
import com.inter.teste_tecnico.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/current-position")
    public ResponseEntity<BigDecimal> getCurrentPosition() {
        log.info("Received request to get current position");
        return ResponseEntity.ok(dashboardService.getCurrentPosition());
    }

    @GetMapping("/most-traded-assets")
    public ResponseEntity<List<AssetTradeCountResponse>> getMostTradedAssets() {
        log.info("Received request to get most traded assets");
        return ResponseEntity.ok(dashboardService.getMostTradedAssets());
    }
}
