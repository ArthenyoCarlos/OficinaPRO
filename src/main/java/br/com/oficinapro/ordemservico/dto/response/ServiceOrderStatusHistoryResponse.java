package br.com.oficinapro.ordemservico.dto.response;

import br.com.oficinapro.ordemservico.domain.ServiceOrderStatusHistory;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderStatusHistoryResponse(
        UUID id,
        ServiceOrderStatus previousStatus,
        ServiceOrderStatus newStatus,
        String changedByCode,
        String changedByName,
        String notes,
        LocalDateTime changedAt
) {
    public ServiceOrderStatusHistoryResponse(ServiceOrderStatusHistory entity) {
        this(
                entity.getId(),
                entity.getPreviousStatus(),
                entity.getNewStatus(),
                entity.getChangedBy() != null ? entity.getChangedBy().getCode() : null,
                entity.getChangedBy() != null ? entity.getChangedBy().getFullName() : null,
                entity.getNotes(),
                entity.getChangedAt()
        );
    }
}
