package br.com.oficinapro.ordemservico.dto.request;

import br.com.oficinapro.financeiro.domain.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceOrderReceiptRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        LocalDateTime receiptDate,

        @Size(max = 1000, message = "Notes must be less than 1000 characters")
        String notes,

        @Size(max = 255, message = "Received by code must be less than 255 characters")
        String receivedByCode
) {
}
