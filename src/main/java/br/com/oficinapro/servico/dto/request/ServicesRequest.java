package br.com.oficinapro.servico.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicesRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 120, message = "Name must be between 3 and 120 characters")
        String name,

        @NotBlank(message = "Category is required")
        @Size(min = 3, max = 80, message = "Category must be between 3 and 80 characters")
        String category,

        @Size(max = 1000, message = "Description must be less than 1000 characters")
        String description,

        @NotNull(message = "Default price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Default price must be zero or greater")
        BigDecimal defaultPrice,

        @NotNull(message = "Estimated time in minutes is required")
        @Min(value = 1, message = "Estimated time in minutes must be greater than zero")
        Integer estimatedTimeMinutes
) {
}
