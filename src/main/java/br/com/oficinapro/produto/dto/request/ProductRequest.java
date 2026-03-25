package br.com.oficinapro.produto.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 120, message = "Name must be between 3 and 120 characters")
        String name,

        @NotBlank(message = "SKU is required")
        @Size(min = 2, max = 60, message = "SKU must be between 2 and 60 characters")
        String sku,

        @NotBlank(message = "Category code is required")
        @Size(max = 255, message = "Category code must be less than 255 characters")
        String categoryCode,

        @NotBlank(message = "Unit is required")
        @Size(min = 1, max = 20, message = "Unit must be between 1 and 20 characters")
        String unit,

        @NotNull(message = "Cost price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Cost price must be zero or greater")
        BigDecimal costPrice,

        @NotNull(message = "Sale price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Sale price must be zero or greater")
        BigDecimal salePrice,

        @NotNull(message = "Current stock is required")
        @PositiveOrZero(message = "Current stock must be zero or greater")
        Integer currentStock,

        @NotNull(message = "Minimum stock is required")
        @Min(value = 0, message = "Minimum stock must be zero or greater")
        Integer minimumStock,

        @Size(max = 255, message = "Supplier code must be less than 255 characters")
        String supplierCode
) {
}
