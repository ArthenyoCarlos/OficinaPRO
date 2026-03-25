package br.com.oficinapro.estoque.service;

import br.com.oficinapro.estoque.domain.StockMovement;
import br.com.oficinapro.estoque.dto.response.StockMovementResponse;
import br.com.oficinapro.estoque.repository.StockMovementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllStockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public FindAllStockMovementService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findAll(Pageable pageable) {
        Page<StockMovement> movements = stockMovementRepository.findAll(pageable);
        return movements.map(StockMovementResponse::new);
    }
}
