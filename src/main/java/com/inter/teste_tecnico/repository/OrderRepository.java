package com.inter.teste_tecnico.repository;

import com.inter.teste_tecnico.model.Order;
import com.inter.teste_tecnico.payload.response.AssetTradeCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByInvestorId(Long investorId);

    @Query("SELECT SUM(o.price * o.quantity) FROM Order o WHERE o.type = com.inter.teste_tecnico.model.OrderType.BUY")
    BigDecimal calculateCurrentPosition();

    @Query("SELECT new com.inter.teste_tecnico.payload.response.AssetTradeCountResponse(a.ticker, COUNT(o)) FROM Order o JOIN o.asset a GROUP BY a.ticker ORDER BY COUNT(o) DESC")
    List<AssetTradeCountResponse> findMostTradedAssets();
}
