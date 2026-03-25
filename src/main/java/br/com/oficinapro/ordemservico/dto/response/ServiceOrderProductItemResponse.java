package br.com.oficinapro.ordemservico.dto.response;

import br.com.oficinapro.ordemservico.domain.ServiceOrderProductItem;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceOrderProductItemResponse(
        UUID id,
        String productCode,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal totalAmount
) {
    public ServiceOrderProductItemResponse(ServiceOrderProductItem entity) {
        this(
                entity.getId(),
                entity.getProduct() != null ? entity.getProduct().getCode() : null,
                entity.getProduct() != null ? entity.getProduct().getName() : null,
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getDiscount(),
                entity.getTotalAmount()
        );
    }
}
