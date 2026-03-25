package br.com.oficinapro.ordemservico.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServiceOrderProductItemRequest(

        @NotBlank(message = "Product code is required")
        @Size(max = 255, message = "Product code must be less than 255 characters")
        String productCode,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than zero")
        Integer quantity,

        @DecimalMin(value = "0.0", inclusive = true, message = "Unit price must be zero or greater")
        BigDecimal unitPrice,

        @DecimalMin(value = "0.0", inclusive = true, message = "Discount must be zero or greater")
        BigDecimal discount,

        @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be zero or greater")
        BigDecimal totalAmount
) {
}
