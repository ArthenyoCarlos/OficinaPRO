package br.com.oficinapro.estoque.dto.request;

import br.com.oficinapro.estoque.domain.enums.StockMovementOriginType;
import br.com.oficinapro.estoque.domain.enums.StockMovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementRequest(

        @NotBlank(message = "Product code is required")
        @Size(max = 255, message = "Product code must be less than 255 characters")
        String productCode,

        @NotNull(message = "Movement type is required")
        StockMovementType movementType,

        @NotNull(message = "Origin type is required")
        StockMovementOriginType originType,

        UUID originId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than zero")
        Integer quantity,

        @NotNull(message = "Previous balance is required")
        @Min(value = 0, message = "Previous balance must be zero or greater")
        Integer previousBalance,

        @NotNull(message = "Later balance is required")
        @Min(value = 0, message = "Later balance must be zero or greater")
        Integer laterBalance,

        @DecimalMin(value = "0.0", inclusive = true, message = "Unit cost must be zero or greater")
        BigDecimal unitCost,

        @Size(max = 1000, message = "Notes must be less than 1000 characters")
        String notes,

        @Size(max = 255, message = "Moved by code must be less than 255 characters")
        String movedByCode,

        LocalDateTime movedAt
) {
}
