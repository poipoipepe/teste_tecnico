package com.inter.teste_tecnico.service;

import com.inter.teste_tecnico.model.Investor;
import com.inter.teste_tecnico.model.Order;
import com.inter.teste_tecnico.payload.request.InvestorRequest;
import com.inter.teste_tecnico.payload.response.InvestorResponse;
import com.inter.teste_tecnico.repository.InvestorRepository;
import com.inter.teste_tecnico.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public InvestorResponse createInvestor(InvestorRequest request) {
        log.info("Creating new investor with name: {}", request.name());
        Investor investor = Investor.builder()
                .name(request.name())
                .build();

        Investor savedInvestor = investorRepository.save(investor);
        log.info("Investor created successfully with ID: {}", savedInvestor.getId());

        return new InvestorResponse(savedInvestor.getId(), savedInvestor.getName());
    }

    @Transactional
    public void deleteInvestor(Long id) {
        log.info("Attempting to delete investor with ID: {}", id);
        if (!investorRepository.existsById(id)) {
            log.error("Failed to delete investor. Investor not found with ID: {}", id);
            throw new IllegalArgumentException("Investor not found with id: " + id);
        }

        log.info("Fetching orders associated with investor ID: {}", id);
        List<Order> orders = orderRepository.findByInvestorId(id);
        if (!orders.isEmpty()) {
            log.info("Deleting {} orders associated with investor ID: {}", orders.size(), id);
            orderRepository.deleteAll(orders);
        }

        investorRepository.deleteById(id);
        log.info("Investor with ID: {} deleted successfully", id);
    }
}
