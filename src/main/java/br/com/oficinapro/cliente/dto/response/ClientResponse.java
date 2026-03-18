package br.com.oficinapro.cliente.dto.response;

import br.com.oficinapro.cliente.domain.Client;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String code,
        String name,
        String cpfCnpj,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String zipCode,
        String observation,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ClientResponse(Client entity){
        this(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getCpfCnpj(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode(),
                entity.getObservation(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
