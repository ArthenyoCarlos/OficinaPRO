package br.com.oficinapro.estoque.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.domain.StockMovement;
import br.com.oficinapro.estoque.dto.response.StockMovementResponse;
import br.com.oficinapro.estoque.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FindByStockMovementIdService {

    private final StockMovementRepository stockMovementRepository;

    public FindByStockMovementIdService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional(readOnly = true)
    public StockMovementResponse findById(UUID id) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        return new StockMovementResponse(stockMovement);
    }
}
