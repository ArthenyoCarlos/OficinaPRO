package br.com.oficinapro.estoque.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.estoque.domain.StockMovement;
import br.com.oficinapro.estoque.domain.enums.StockMovementOriginType;
import br.com.oficinapro.estoque.domain.enums.StockMovementType;
import br.com.oficinapro.estoque.repository.StockMovementRepository;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.ServiceOrderProductItem;
import br.com.oficinapro.produto.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderStockService {

    private final StockMovementRepository stockMovementRepository;

    public ServiceOrderStockService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public void outputProducts(ServiceOrder serviceOrder, User movedBy) {
        List<StockMovement> movements = new ArrayList<>();

        for (ServiceOrderProductItem item : serviceOrder.getProductItems()) {
            Product product = item.getProduct();
            int currentStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
            int quantity = item.getQuantity() != null ? item.getQuantity() : 0;

            if (quantity <= 0) {
                continue;
            }

            if (currentStock < quantity) {
                throw new BusinessException("Insufficient stock for product " + product.getCode());
            }

            int laterStock = currentStock - quantity;
            product.setCurrentStock(laterStock);

            StockMovement movement = new StockMovement();
            movement.setProduct(product);
            movement.setMovementType(StockMovementType.EXIT);
            movement.setOriginType(StockMovementOriginType.SERVICE_ORDER);
            movement.setOriginId(serviceOrder.getId());
            movement.setQuantity(quantity);
            movement.setPreviousBalance(currentStock);
            movement.setLaterBalance(laterStock);
            movement.setUnitCost(product.getCostPrice());
            movement.setNotes("Stock output for service order " + serviceOrder.getNumber());
            movement.setMovedBy(movedBy);
            movement.setMovedAt(LocalDateTime.now());
            movements.add(movement);
        }

        if (!movements.isEmpty()) {
            stockMovementRepository.saveAll(movements);
        }
    }

    @Transactional
    public void returnProducts(ServiceOrder serviceOrder, User movedBy) {
        List<StockMovement> movements = new ArrayList<>();

        for (ServiceOrderProductItem item : serviceOrder.getProductItems()) {
            Product product = item.getProduct();
            int currentStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
            int quantity = item.getQuantity() != null ? item.getQuantity() : 0;

            if (quantity <= 0) {
                continue;
            }

            int laterStock = currentStock + quantity;
            product.setCurrentStock(laterStock);

            StockMovement movement = new StockMovement();
            movement.setProduct(product);
            movement.setMovementType(StockMovementType.ENTRY);
            movement.setOriginType(StockMovementOriginType.SERVICE_ORDER);
            movement.setOriginId(serviceOrder.getId());
            movement.setQuantity(quantity);
            movement.setPreviousBalance(currentStock);
            movement.setLaterBalance(laterStock);
            movement.setUnitCost(product.getCostPrice());
            movement.setNotes("Stock return for canceled service order " + serviceOrder.getNumber());
            movement.setMovedBy(movedBy);
            movement.setMovedAt(LocalDateTime.now());
            movements.add(movement);
        }

        if (!movements.isEmpty()) {
            stockMovementRepository.saveAll(movements);
        }
    }
}
