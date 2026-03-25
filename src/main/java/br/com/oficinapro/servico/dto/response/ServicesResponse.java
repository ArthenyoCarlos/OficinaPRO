package br.com.oficinapro.servico.dto.response;

import br.com.oficinapro.servico.domain.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ServicesResponse(
        UUID id,
        String code,
        String name,
        String category,
        String description,
        BigDecimal defaultPrice,
        Integer estimatedTimeMinutes,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ServicesResponse(Services entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getDefaultPrice(),
                entity.getEstimatedTimeMinutes(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
