package br.com.oficinapro.fornecedor.dto.response;

import br.com.oficinapro.fornecedor.domain.Supplier;

import java.time.LocalDateTime;
import java.util.UUID;

public record SupplierResponse(
        UUID id,
        String code,
        String name,
        String cnpj,
        String phone,
        String email,
        String contact,
        String observation,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public SupplierResponse(Supplier entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getCnpj(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getContact(),
                entity.getObservation(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
