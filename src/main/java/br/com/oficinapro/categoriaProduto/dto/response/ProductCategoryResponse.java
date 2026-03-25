package br.com.oficinapro.categoriaProduto.dto.response;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductCategoryResponse(
        UUID id,
        String code,
        String name,
        String description,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProductCategoryResponse(ProductCategory entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
