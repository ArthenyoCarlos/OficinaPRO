package br.com.oficinapro.ordemservico.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelServiceOrderRequest(

        @Size(max = 255, message = "Changed by code must be less than 255 characters")
        String changedByCode,

        @NotBlank(message = "Cancellation notes are required")
        @Size(max = 2000, message = "Notes must be less than 2000 characters")
        String notes
) {
}
