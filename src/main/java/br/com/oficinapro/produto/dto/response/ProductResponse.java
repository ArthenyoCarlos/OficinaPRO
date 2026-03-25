package br.com.oficinapro.produto.dto.response;

import br.com.oficinapro.produto.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String code,
        String name,
        String sku,
        String categoryCode,
        String categoryName,
        String unit,
        BigDecimal costPrice,
        BigDecimal salePrice,
        Integer currentStock,
        Integer minimumStock,
        String supplierCode,
        String supplierName,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProductResponse(Product entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getSku(),
                entity.getCategory() != null ? entity.getCategory().getCode() : null,
                entity.getCategory() != null ? entity.getCategory().getName() : null,
                entity.getUnit(),
                entity.getCostPrice(),
                entity.getSalePrice(),
                entity.getCurrentStock(),
                entity.getMinimumStock(),
                entity.getSupplier() != null ? entity.getSupplier().getCode() : null,
                entity.getSupplier() != null ? entity.getSupplier().getName() : null,
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
