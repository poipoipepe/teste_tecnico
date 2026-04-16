package com.inter.teste_tecnico.controller;

import com.inter.teste_tecnico.payload.response.AssetPositionResponse;
import com.inter.teste_tecnico.payload.response.TradedResponse;
import com.inter.teste_tecnico.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/investors/{investorId}/current-position")
    public ResponseEntity<List<AssetPositionResponse>> getCurrentInvestorPosition(@PathVariable Long investorId) {
        log.info("Received request to get current position for investor ID: {}", investorId);
        return ResponseEntity.ok(dashboardService.getCurrentInvestorPosition(investorId));
    }

    @GetMapping("/most-traded-assets")
    public ResponseEntity<List<TradedResponse>> getMostTradedAssets(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        log.info("Received request to get most traded assets from {} to {}", from, to);
        return ResponseEntity.ok(dashboardService.getMostTradedAssets(from, to));
    }
}
