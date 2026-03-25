package br.com.oficinapro.financeiro.dto.response;

import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptResponse(
        UUID id,
        String serviceOrderCode,
        String serviceOrderNumber,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        LocalDateTime receiptDate,
        String notes,
        String receivedByCode,
        String receivedByName
) {
    public ReceiptResponse(Receipt entity) {
        this(
                entity.getId(),
                entity.getServiceOrder() != null ? entity.getServiceOrder().getCode() : null,
                entity.getServiceOrder() != null ? entity.getServiceOrder().getNumber() : null,
                entity.getAmount(),
                entity.getPaymentMethod(),
                entity.getReceiptDate(),
                entity.getNotes(),
                entity.getReceivedBy() != null ? entity.getReceivedBy().getCode() : null,
                entity.getReceivedBy() != null ? entity.getReceivedBy().getFullName() : null
        );
    }
}
