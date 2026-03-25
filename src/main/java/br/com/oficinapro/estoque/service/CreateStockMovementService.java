package br.com.oficinapro.estoque.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.domain.StockMovement;
import br.com.oficinapro.estoque.domain.enums.StockMovementType;
import br.com.oficinapro.estoque.dto.request.StockMovementRequest;
import br.com.oficinapro.estoque.dto.response.StockMovementResponse;
import br.com.oficinapro.estoque.mapper.StockMovementMapper;
import br.com.oficinapro.estoque.repository.StockMovementRepository;
import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CreateStockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CreateStockMovementService(StockMovementRepository stockMovementRepository,
                                      StockMovementMapper stockMovementMapper,
                                      ProductRepository productRepository,
                                      UserRepository userRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.stockMovementMapper = stockMovementMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public StockMovementResponse create(StockMovementRequest request) {
        validateBalances(request);
        StockMovement stockMovement = stockMovementMapper.toEntity(request);
        stockMovement.setProduct(findProduct(request.productCode()));
        stockMovement.setMovedBy(findUser(request.movedByCode()));
        stockMovement.setMovedAt(request.movedAt() != null ? request.movedAt() : LocalDateTime.now());

        return stockMovementMapper.toResponse(stockMovementRepository.save(stockMovement));
    }

    private void validateBalances(StockMovementRequest request) {
        int expectedLater = switch (request.movementType()) {
            case ENTRY -> request.previousBalance() + request.quantity();
            case EXIT -> request.previousBalance() - request.quantity();
            case ADJUSTMENT -> request.laterBalance();
        };

        if (request.movementType() != StockMovementType.ADJUSTMENT && expectedLater != request.laterBalance()) {
            throw new BusinessException("Later balance does not match movement calculation");
        }
    }

    private Product findProduct(String code) {
        return productRepository.findByCode(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + code));
    }

    private User findUser(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return userRepository.findByCode(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with code: " + code));
    }
}
