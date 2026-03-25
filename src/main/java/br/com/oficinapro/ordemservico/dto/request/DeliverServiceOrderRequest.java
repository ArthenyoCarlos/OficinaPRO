package br.com.oficinapro.ordemservico.dto.request;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DeliverServiceOrderRequest(

        @Size(max = 255, message = "Changed by code must be less than 255 characters")
        String changedByCode,

        LocalDateTime deliveredAt,

        @Size(max = 2000, message = "Notes must be less than 2000 characters")
        String notes
) {
}
