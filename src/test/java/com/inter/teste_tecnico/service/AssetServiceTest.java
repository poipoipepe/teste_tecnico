package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.model.Asset;
import com.inter.teste_tecnico.payload.request.AssetRequest;
import com.inter.teste_tecnico.payload.response.AssetResponse;
import com.inter.teste_tecnico.repository.AssetRepository;
import com.inter.teste_tecnico.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private AssetService assetService;

    @Test
    void createAssetShouldReturnCreatedAssetWithUppercaseTicker() {
        AssetRequest request = new AssetRequest(
                "petr4",
                "Petrobras",
                "Energia",
                new BigDecimal("35.10")
        );

        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> {
            Asset assetToSave = invocation.getArgument(0);
            assetToSave.setId(1L);
            return assetToSave;
        });

        AssetResponse response = assetService.createAsset(request);

        assertEquals(1L, response.id());
        assertEquals("PETR4", response.ticker());
        assertEquals("Petrobras", response.companyName());
        assertEquals("Energia", response.sector());
        assertEquals(new BigDecimal("35.10"), response.currentValue());
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void deleteAssetShouldThrowWhenAssetDoesNotExist() {
        Long assetId = 99L;
        when(assetRepository.existsById(assetId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assetService.deleteAsset(assetId)
        );

        assertEquals("Asset not found with id: 99", exception.getMessage());
        verify(assetRepository).existsById(assetId);
        verify(orderRepository, never()).existsByAssetId(any(Long.class));
        verify(assetRepository, never()).deleteById(any(Long.class));
    }
}
