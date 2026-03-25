package br.com.oficinapro.estoque.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteStockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public DeleteStockMovementService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public void delete(UUID id) {
        if (!stockMovementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stock movement not found with id: " + id);
        }
        stockMovementRepository.deleteById(id);
    }
}
