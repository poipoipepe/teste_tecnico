package com.inter.teste_tecnico.repository;

import com.inter.teste_tecnico.model.Order;
import com.inter.teste_tecnico.payload.response.AssetPositionResponse;
import com.inter.teste_tecnico.payload.response.TradedResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByInvestorId(Long investorId);

    boolean existsByAssetId(Long assetId);

    @Query("""
            SELECT new com.inter.teste_tecnico.payload.response.AssetPositionResponse(
                a.ticker,
                SUM(CASE WHEN o.type = com.inter.teste_tecnico.model.OrderType.BUY THEN o.quantity ELSE -o.quantity END),
                SUM(CASE WHEN o.type = com.inter.teste_tecnico.model.OrderType.BUY THEN o.price * o.quantity ELSE -(o.price * o.quantity) END)
            )
            FROM Order o JOIN o.asset a
            WHERE o.investor.id = :investorId
            GROUP BY a.ticker
            """)
    List<AssetPositionResponse> findPositionByInvestorId(@Param("investorId") Long investorId);

    @Query("""
            SELECT new com.inter.teste_tecnico.payload.response.TradedResponse(
                a.ticker,
                SUM(CASE WHEN o.type = com.inter.teste_tecnico.model.OrderType.BUY THEN 1 ELSE 0 END),
                SUM(CASE WHEN o.type = com.inter.teste_tecnico.model.OrderType.SELL THEN 1 ELSE 0 END)
            )
            FROM Order o JOIN o.asset a
            WHERE (:from IS NULL OR o.timestamp >= :from) AND (:to IS NULL OR o.timestamp <= :to)
            GROUP BY a.ticker
            ORDER BY COUNT(o) DESC
            """)
    List<TradedResponse> findMostTradedAssets(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
