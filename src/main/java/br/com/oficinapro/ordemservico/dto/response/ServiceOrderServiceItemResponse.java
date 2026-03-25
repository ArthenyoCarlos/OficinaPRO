package br.com.oficinapro.ordemservico.dto.response;

import br.com.oficinapro.ordemservico.domain.ServiceOrderServiceItem;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceOrderServiceItemResponse(
        UUID id,
        String serviceCode,
        String serviceName,
        String complementaryDescription,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal totalAmount,
        String responsibleTechnicianCode,
        String responsibleTechnicianName
) {
    public ServiceOrderServiceItemResponse(ServiceOrderServiceItem entity) {
        this(
                entity.getId(),
                entity.getService() != null ? entity.getService().getCode() : null,
                entity.getService() != null ? entity.getService().getName() : null,
                entity.getComplementaryDescription(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getDiscount(),
                entity.getTotalAmount(),
                entity.getResponsibleTechnician() != null ? entity.getResponsibleTechnician().getCode() : null,
                entity.getResponsibleTechnician() != null ? entity.getResponsibleTechnician().getFullName() : null
        );
    }
}
