package br.com.oficinapro.veiculo.dto.response;

import br.com.oficinapro.veiculo.domain.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String code,
        String plate,
        String chassis,
        String renavam,
        String brand,
        String model,
        Integer manufactureYear,
        Integer modelYear,
        String color,
        String fuelType,
        Long currentMileage,
        String clientCode,
        String notes,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public VehicleResponse(Vehicle entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getPlate(),
                entity.getChassis(),
                entity.getRenavam(),
                entity.getBrand(),
                entity.getModel(),
                entity.getManufactureYear(),
                entity.getModelYear(),
                entity.getColor(),
                entity.getFuelType(),
                entity.getCurrentMileage(),
                entity.getClient() != null ? entity.getClient().getName() : null,
                entity.getNotes(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
