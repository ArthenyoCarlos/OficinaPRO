package br.com.oficinapro.ordemservico.dto.request;

import br.com.oficinapro.financeiro.domain.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinishServiceOrderRequest(

        @Size(max = 255, message = "Changed by code must be less than 255 characters")
        String changedByCode,

        LocalDateTime finalizedAt,

        @DecimalMin(value = "0.0", inclusive = false, message = "Receipt amount must be greater than zero")
        BigDecimal receiptAmount,

        PaymentMethod paymentMethod,

        LocalDateTime receiptDate,

        @Size(max = 1000, message = "Receipt notes must be less than 1000 characters")
        String receiptNotes,

        @Size(max = 255, message = "Received by code must be less than 255 characters")
        String receivedByCode,

        @Size(max = 2000, message = "Notes must be less than 2000 characters")
        String notes
) {
}
