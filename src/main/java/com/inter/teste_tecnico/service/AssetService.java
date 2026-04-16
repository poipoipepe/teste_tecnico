package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.payload.response.AssetResponse;
import com.inter.teste_tecnico.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final AssetRepository assetRepository;

    public List<AssetResponse> findAll() {
        log.info("Fetching all assets from database");
        List<AssetResponse> assets = assetRepository.findAll().stream()
                .map(asset -> new AssetResponse(asset.getId(), asset.getTicker(), asset.getCompanyName(), asset.getSector()))
                .toList();
        log.info("Found {} assets", assets.size());
        return assets;
    }
}
