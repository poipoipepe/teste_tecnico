package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.model.Asset;
import com.inter.teste_tecnico.payload.request.AssetRequest;
import com.inter.teste_tecnico.payload.response.AssetResponse;
import com.inter.teste_tecnico.repository.AssetRepository;
import com.inter.teste_tecnico.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final AssetRepository assetRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<AssetResponse> findAll() {
        log.info("Fetching all assets from database");
        List<AssetResponse> assets = assetRepository.findAll().stream()
            .map(asset -> new AssetResponse(
                asset.getId(),
                asset.getTicker(),
                asset.getCompanyName(),
                asset.getSector(),
                asset.getCurrentValue()))
                .toList();
        log.info("Found {} assets", assets.size());
        return assets;
    }

    @Transactional
    public AssetResponse createAsset(AssetRequest request) {
        log.info("Creating new asset with ticker: {}", request.ticker());
        Asset asset = Asset.builder()
                .ticker(request.ticker().toUpperCase())
                .companyName(request.companyName())
                .sector(request.sector())
            .currentValue(request.currentValue())
                .build();

        Asset savedAsset = assetRepository.save(asset);
        log.info("Asset created successfully with ID: {}", savedAsset.getId());
        return new AssetResponse(
            savedAsset.getId(),
            savedAsset.getTicker(),
            savedAsset.getCompanyName(),
            savedAsset.getSector(),
            savedAsset.getCurrentValue());
        }

        @Transactional
        public AssetResponse updateCurrentValue(Long id, BigDecimal currentValue) {
        log.info("Updating current value for asset with ID: {}", id);
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + id));

        asset.setCurrentValue(currentValue);
        Asset updatedAsset = assetRepository.save(asset);

        return new AssetResponse(
            updatedAsset.getId(),
            updatedAsset.getTicker(),
            updatedAsset.getCompanyName(),
            updatedAsset.getSector(),
            updatedAsset.getCurrentValue());
    }

    @Transactional
    public void deleteAsset(Long id) {
        log.info("Attempting to delete asset with ID: {}", id);
        if (!assetRepository.existsById(id)) {
            log.error("Asset not found with ID: {}", id);
            throw new IllegalArgumentException("Asset not found with id: " + id);
        }
        if (orderRepository.existsByAssetId(id)) {
            log.error("Cannot delete asset ID: {} — it has associated orders", id);
            throw new IllegalArgumentException("Cannot delete asset with id: " + id + " because it has associated orders");
        }
        assetRepository.deleteById(id);
        log.info("Asset with ID: {} deleted successfully", id);
    }
}
