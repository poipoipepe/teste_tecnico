package com.inter.teste_tecnico.controller;

import com.inter.teste_tecnico.payload.request.AssetCurrentValueRequest;
import com.inter.teste_tecnico.payload.request.AssetRequest;
import com.inter.teste_tecnico.payload.response.AssetResponse;
import com.inter.teste_tecnico.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        log.info("Received request to get all assets");
        return ResponseEntity.ok(assetService.findAll());
    }

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(@Valid @RequestBody AssetRequest request) {
        log.info("Received request to create asset with ticker: {}", request.ticker());
        AssetResponse createdAsset = assetService.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    @PatchMapping("/{id}/current-value")
    public ResponseEntity<AssetResponse> updateCurrentValue(@PathVariable Long id, @Valid @RequestBody AssetCurrentValueRequest request) {
        log.info("Received request to update current value for asset id: {}", id);
        return ResponseEntity.ok(assetService.updateCurrentValue(id, request.currentValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        log.info("Received request to delete asset with id: {}", id);
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
