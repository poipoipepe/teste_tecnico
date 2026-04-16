package com.inter.teste_tecnico.controller;

import com.inter.teste_tecnico.payload.request.AssetRequest;
import com.inter.teste_tecnico.payload.response.AssetResponse;
import com.inter.teste_tecnico.service.AssetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetControllerTest {

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    @Test
    void getAllAssetsShouldReturnStatusOkAndAssetList() {
        List<AssetResponse> assets = List.of(
                new AssetResponse(1L, "PETR4", "Petrobras", "Oil & Gas", new BigDecimal("48.58"))
        );
        when(assetService.findAll()).thenReturn(assets);

        ResponseEntity<List<AssetResponse>> response = assetController.getAllAssets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assets, response.getBody());
        verify(assetService).findAll();
    }

    @Test
    void createAssetShouldReturnStatusCreatedAndCreatedAsset() {
        AssetRequest request = new AssetRequest(
                "PETR4",
                "Petrobras",
                "Oil & Gas",
                new BigDecimal("48.58")
        );
        AssetResponse createdAsset = new AssetResponse(
                1L,
                "PETR4",
                "Petrobras",
                "Oil & Gas",
                new BigDecimal("48.58")
        );
        when(assetService.createAsset(request)).thenReturn(createdAsset);

        ResponseEntity<AssetResponse> response = assetController.createAsset(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdAsset, response.getBody());
        verify(assetService).createAsset(request);
    }
}