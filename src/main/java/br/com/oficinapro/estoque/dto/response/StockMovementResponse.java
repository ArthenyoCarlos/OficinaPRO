package br.com.oficinapro.estoque.dto.response;

import br.com.oficinapro.estoque.domain.StockMovement;
import br.com.oficinapro.estoque.domain.enums.StockMovementOriginType;
import br.com.oficinapro.estoque.domain.enums.StockMovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementResponse(
        UUID id,
        String productCode,
        String productName,
        StockMovementType movementType,
        StockMovementOriginType originType,
        UUID originId,
        Integer quantity,
        Integer previousBalance,
        Integer laterBalance,
        BigDecimal unitCost,
        String notes,
        String movedByCode,
        String movedByName,
        LocalDateTime movedAt
) {
    public StockMovementResponse(StockMovement entity) {
        this(
                entity.getId(),
                entity.getProduct() != null ? entity.getProduct().getCode() : null,
                entity.getProduct() != null ? entity.getProduct().getName() : null,
                entity.getMovementType(),
                entity.getOriginType(),
                entity.getOriginId(),
                entity.getQuantity(),
                entity.getPreviousBalance(),
                entity.getLaterBalance(),
                entity.getUnitCost(),
                entity.getNotes(),
                entity.getMovedBy() != null ? entity.getMovedBy().getCode() : null,
                entity.getMovedBy() != null ? entity.getMovedBy().getFullName() : null,
                entity.getMovedAt()
        );
    }
}
